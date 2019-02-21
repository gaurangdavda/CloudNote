#!/bin/bash

set -e
## Checking whether the stack-name is passed as an arguement
if [ $# -lt 1 ]; then
  echo "Kindly provide stack name! Script execution stopped."
  exit 1
fi
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "Creating cloud stack with name: $1"
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"

# stackList=$(aws cloudformation list-stack-resources --stack-name $1)
#
# if [ ! -z "$stackList" ]; then
#   echo "Stack already exists. Please use a different name for the stack"
#   echo "Stack creation failed. Exiting..."
#   echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
#   exit 1
# fi

echo "Displaying all keys!"
aws ec2 describe-key-pairs 
echo -e "\n"
echo "Choose 1 Key which you want to use!"
read KEY_CHOSEN

echo "Displaying AMI!"
aws ec2 describe-images --owners self --query 'Images[*].{ID:ImageId}'
echo -e "\n"
echo "Enter AMI ID"
read amiId

stackList=$(aws cloudformation list-stacks --query 'StackSummaries[?StackStatus != `DELETE_COMPLETE`].{StackName:StackName}')
#echo "stacklist is $stackList"


if [  `echo $stackList | grep -w -c $1 ` -gt 0 ]
then
  echo "Stack with name: $1  exists"
  echo "Stack creation failed"
  echo "Exiting.."
  exit 1
fi

#Replacing the STACK_NAME passed by the user in the csye6225-cf-networking-parameters.json
sed -i "s/REPLACE_STACK_NAME/$1/g" csye6225-cf-networking-parameters.json

##Creating Stack
#echo "Creating Cloud Stack $1"
response=$(aws cloudformation create-stack --stack-name "$1" --template-body file://csye6225-cf-application.json --parameters ParameterKey="keyname",ParameterValue=$KEY_CHOSEN ParameterKey="AmiId",ParameterValue=$amiId)
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "Waiting for Stack $1 to be created"
echo "$response"

aws cloudformation wait stack-create-complete --stack-name $1
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "stack $1 created successfully"
sed -i "s/$1/REPLACE_STACK_NAME/g" csye6225-cf-networking-parameters.json
