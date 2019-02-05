#!/bin/bash
clear
if [ $# -gt 0 ]
then
	echo "VPC ID is, $1."
	echo "Number of arguments: $#"
	vpcid=$1
	# Delete subnets
	echo "Deleting Subnets"
	for i in `aws ec2 describe-subnets --filters Name=vpc-id,Values="${vpcid}" | grep SubnetId | sed -E 's/^.*(subnet-[a-z0-9]+).*$/\1/'`;
	do
		aws ec2 delete-subnet --subnet-id=$i;
		echo "Deleted Subnets"
	done

	# Delete route tables
	echo "Deleting route tables"
	for i in `aws ec2 describe-route-tables --filters Name=vpc-id,Values="${vpcid}" --query "RouteTables[*].RouteTableId" --output text | tr -d '"'`;
	do
		aws ec2 delete-route-table --route-table-id=$i;
		echo "Deleted route tables"
	done

	# Detach & Delete internet gateways
	for i in `aws ec2 describe-internet-gateways --filters Name=attachment.vpc-id,Values="${vpcid}" | grep igw- | sed -E 's/^.*(igw-[a-z0-9]+).*$/\1/'`;
	do
		echo "Detaching internet gateways"
		aws ec2 detach-internet-gateway --internet-gateway-id=$i --vpc-id=${vpcid};
		echo "Detached internet gateways"
		echo "Deleting internet gateways"
		aws ec2 delete-internet-gateway --internet-gateway-id=$i;
		echo "Deleted internet gateways"
	done

	# Delete the VPC
	echo "Deleting VPC"
	aws ec2 delete-vpc --vpc-id ${vpcid}
	echo "Deleted VPC"
else
	echo "WARNING: No argument specified! Kindly specify VPC ID as the only argument."
fi
