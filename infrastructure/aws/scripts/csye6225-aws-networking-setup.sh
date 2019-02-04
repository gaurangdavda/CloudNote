#!/bin/bash
vpcCidrBlock="10.0.0.0/16"
subnetcidrblock0="10.0.0.0/24"
subnetcidrblock1="10.0.1.0/24"
subnetcidrblock2="10.0.2.0/24"
availabilityZone1="us-east-1a"
availabilityZone2="us-east-1b"
availabilityZone3="us-east-1c"
destinationCidrBlock="0.0.0.0/0"
securityGroupName="securityGroup"
port22CidrBlock="0.0.0.0/0"

set -e

echo "Creating VPC.."
aws_response=$(aws ec2 create-vpc --cidr-bloc $vpcCidrBlock --output json)
echo "VPC Created"
vpcId=$(echo -e "$aws_response" |  /usr/bin/jq '.Vpc.VpcId' | tr -d '"')

echo "Creating subnets"
subnet_response0=$(aws ec2 create-subnet --vpc-id "$vpcId" --cidr-block $subnetcidrblock0 --availability-zone=$availabilityZone1 || echo "error occured")
subnetId0=$(echo -e "$subnet_response0" |  /usr/bin/jq '.Subnet.SubnetId' | tr -d '"')

subnet_response1=$(aws ec2 create-subnet --vpc-id "$vpcId" --cidr-block $subnetcidrblock1 --availability-zone=$availabilityZone2)
subnetId1=$(echo -e "$subnet_response1" |  /usr/bin/jq '.Subnet.SubnetId' | tr -d '"')

subnet_response2=$(aws ec2 create-subnet --vpc-id "$vpcId" --cidr-block $subnetcidrblock2 --availability-zone=$availabilityZone3)
subnetId2=$(echo -e "$subnet_response2" |  /usr/bin/jq '.Subnet.SubnetId' | tr -d '"')
echo "Subnets created"

echo "Creating Internet Gateway"
gateway_response=$(aws ec2 create-internet-gateway --output json)
gatewayId=$(echo -e "$gateway_response" |  /usr/bin/jq '.InternetGateway.InternetGatewayId' | tr -d '"')
echo "Internet Gateway Created"

echo "Attaching Internet Gateway to VPC"
attach_response=$(aws ec2 attach-internet-gateway --internet-gateway-id "$gatewayId"  --vpc-id "$vpcId")
echo "Gateway Attached"

echo "Creating Route Table for VPC"
route_table_response=$(aws ec2 create-route-table --vpc-id "$vpcId" --output json)
routeTableId=$(echo -e "$route_table_response" |  /usr/bin/jq '.RouteTable.RouteTableId' | tr -d '"')
echo "Route Table created"

echo "adding route for the internet gateway"
route_response=$(aws ec2 create-route --route-table-id "$routeTableId" --destination-cidr-block "$destinationCidrBlock" --gateway-id "$gatewayId")
echo "Route added to the internet gateway"

echo "associate route table to a subnet"
associate_response=$(aws ec2 associate-route-table --subnet-id "$subnetId0" --route-table-id "$routeTableId")
echo "End"

echo "Security Groups"
#security_response=$(aws ec2 create-security-group --group-name "$securityGroupName" --description "Private: $securityGroupName" --vpc-id "$vpcId" --output json)
#groupId=$(echo -e "$security_response" |  /usr/bin/jq '.GroupId' | tr -d '"')
#security_response2=$(aws ec2 authorize-security-group-ingress --group-id "$groupId" --protocol tcp --port 22 --cidr "$port22CidrBlock")
security_response2=$(aws ec2 authorize-security-group-ingress --group-name default --protocol tcp --port 22 --cidr "$port22CidrBlock")

echo "Security Group created and rules have been updated"
