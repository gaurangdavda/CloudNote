#!/bin/bash
subnetcidrblock0="10.0.0.0/24"
subnetcidrblock1="10.0.1.0/24"
subnetcidrblock2="10.0.2.0/24"
#destinationCidrBlock="0.0.0.0/0"
securityGroupName="securityGroup"
port22CidrBlock="0.0.0.0/0"

set -e

read -p "Enter VPC Cidr block: " vpcCidrBlock
if [ $# -eq 1 ]; then
  echo "Kindly provide 1 VPC Cidr block"
  exit 1
else
  echo "Creating VPC.."
  aws_response=$(aws ec2 create-vpc --cidr-bloc $vpcCidrBlock --output json)
  echo "VPC Created"
  vpcId=$(echo -e "$aws_response" |  /usr/bin/jq '.Vpc.VpcId' | tr -d '"')
fi

echo "Creating Route Table for VPC"
route_table_response=$(aws ec2 create-route-table --vpc-id "$vpcId" --output json)
routeTableId=$(echo -e "$route_table_response" |  /usr/bin/jq '.RouteTable.RouteTableId' | tr -d '"')
echo "Route Table created"

echo "please select region from below: "
for region in `aws ec2 describe-availability-zones --output text | cut -f5`
do
     echo -e $region
done


for i in {1..3}
do
  read -p "Please enter availability zone from above and subnetcidrblock: " availabilityZone subnetcidrblock
  subnet_response=$(aws ec2 create-subnet --vpc-id "$vpcId" --cidr-block $subnetcidrblock --availability-zone=$availabilityZone)
  subnetId=$(echo -e "$subnet_response" |  /usr/bin/jq '.Subnet.SubnetId' | tr -d '"')
  associate_response=$(aws ec2 associate-route-table --subnet-id "$subnetId" --route-table-id "$routeTableId")
done


echo "Creating Internet Gateway"
gateway_response=$(aws ec2 create-internet-gateway --output json)
gatewayId=$(echo -e "$gateway_response" |  /usr/bin/jq '.InternetGateway.InternetGatewayId' | tr -d '"')
echo "Internet Gateway Created"

echo "Attaching Internet Gateway to VPC"
attach_response=$(aws ec2 attach-internet-gateway --internet-gateway-id "$gatewayId"  --vpc-id "$vpcId")
echo "Gateway Attached"

read -p "Please enter destination cidr block for adding route to the internet gateway: " destinationCidrBlock
if [ $# -eq 1 ]; then
  echo "Kindly provide destination cidr block"
  exit 1
else
  route_response=$(aws ec2 create-route --route-table-id "$routeTableId" --destination-cidr-block "$destinationCidrBlock" --gateway-id "$gatewayId")
  echo "Route added to the internet gateway"
fi

echo "Security Groups"
#security_response=$(aws ec2 create-security-group --group-name "$securityGroupName" --description "Private: $securityGroupName" --vpc-id "$vpcId" --output json)
#groupId=$(echo -e "$security_response" |  /usr/bin/jq '.GroupId' | tr -d '"')
#security_response2=$(aws ec2 authorize-security-group-ingress --group-id "$groupId" --protocol tcp --port 22 --cidr "$port22CidrBlock")

#security_response2=$(aws ec2 authorize-security-group-ingress --group-name default --protocol tcp --port 22 --cidr "$port22CidrBlock")
groupdetails=$(aws ec2 describe-security-groups --query "SecurityGroups[*].{ID:GroupId}")
groupid=$(echo -e "$groupdetails" |  /usr/bin/jq '.[0].ID' | tr -d '"')
security_response=$(aws ec2 authorize-security-group-ingress --group-id "$groupid" --protocol tcp --port 22 --cidr "$port22CidrBlock")



echo "Security Group created and rules have been updated"
