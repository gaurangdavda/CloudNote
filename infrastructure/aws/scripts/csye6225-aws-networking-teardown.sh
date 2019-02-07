#!/bin/bash
clear

f () {
    errcode=$? # save the exit code as the first thing done in the trap function
    echo "error $errcode"
    echo "the command executing at the time of the error was"
    echo "$BASH_COMMAND"
    echo "on line ${BASH_LINENO[0]}"
    exit $errcode
}

trap f ERR
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
read -p "Enter VPC-ID to be deleted: " vpcid
if [[ -z "$vpcid" ]];
then
	echo "Kindly provide valid vpc-id"
	exit 1
else
	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"	
	echo "Deleting vpc dependencies..."
	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Revoking security group rules..."
	groupdetails=$(aws ec2 describe-security-groups --filters "Name=vpc-id,Values=$vpcid" --output json)
	groupid=$(echo -e "$groupdetails" |  /usr/bin/jq  '.SecurityGroups[0].GroupId' | tr -d '"')
	cidr=$(echo -e "$groupdetails" |  /usr/bin/jq  '.SecurityGroups[0].IpPermissions[0].IpRanges[0].CidrIp' | tr -d '"')
	aws ec2 revoke-security-group-ingress --group-id $groupid --protocol tcp --port 80 --cidr $cidr
	aws ec2 revoke-security-group-ingress --group-id $groupid --protocol tcp --port 22 --cidr $cidr
	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Revoked security group rules"

	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Disassociating subnets from route table..."	
	mainval=$(aws ec2 describe-route-tables --filters Name=vpc-id,Values="$vpcid" --query 'RouteTables[0].Associations[0].Main' | tr -d '", []')
	if [ $mainval == "false" ];
	then
		routeTableIndex=0
	else
		routeTableIndex=1
	fi

	assoIds=$(aws ec2 describe-route-tables --filters Name=vpc-id,Values="$vpcid" --query 'RouteTables['$routeTableIndex'].Associations[*].RouteTableAssociationId' | tr -d '", []')
	routeTableId=$(aws ec2 describe-route-tables --filters Name=vpc-id,Values="$vpcid" --query 'RouteTables['$routeTableIndex'].RouteTableId' | tr -d '", []')
	originRoute=$(aws ec2 describe-route-tables --filters Name=vpc-id,Values="$vpcid" --query 'RouteTables['$routeTableIndex'].Routes[0].Origin' | tr -d '"')

	if [ $originRoute == "CreateRoute" ];
	then
		destCidr=$(aws ec2 describe-route-tables --filters Name=vpc-id,Values="$vpcid" --query 'RouteTables['$routeTableIndex'].Routes[0].DestinationCidrBlock' | tr -d '"')
	else
		destCidr=$(aws ec2 describe-route-tables --filters Name=vpc-id,Values="$vpcid" --query 'RouteTables['$routeTableIndex'].Routes[1].DestinationCidrBlock' | tr -d '"')
	fi

	for i in $assoIds
	do
		aws ec2 disassociate-route-table --association-id $i
	done
	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Disassociated subnets from route table"

	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Deleting route to destination cidr..."
	aws ec2 delete-route --route-table-id $routeTableId --destination-cidr-block $destCidr
	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Deleted route to destination cidr"
	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Deleting route table..."
	aws ec2 delete-route-table --route-table-id=$routeTableId
	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Deleted route table"

	
	for i in `aws ec2 describe-internet-gateways --filters Name=attachment.vpc-id,Values="${vpcid}" | grep igw- | sed -E 's/^.*(igw-[a-z0-9]+).*$/\1/'`;
	do
		echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
		echo "Detaching internet gateways..."
		aws ec2 detach-internet-gateway --internet-gateway-id=$i --vpc-id=${vpcid};
		echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
		echo "Detached internet gateways"
		echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
		echo "Deleting internet gateways..."
		aws ec2 delete-internet-gateway --internet-gateway-id=$i;
		echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
		echo "Deleted internet gateways"
	done

	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Deleting Subnets..."
	for i in `aws ec2 describe-subnets --filters Name=vpc-id,Values="${vpcid}" | grep SubnetId | sed -E 's/^.*(subnet-[a-z0-9]+).*$/\1/'`;
	do
		aws ec2 delete-subnet --subnet-id=$i;
	done
	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Deleted Subnets"

	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Deleting VPC..."
	aws ec2 delete-vpc --vpc-id ${vpcid}
	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
	echo "Deleted VPC and all its dependencies successfully !!"
	echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
fi
