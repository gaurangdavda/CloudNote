#!/bin/bash

set -e
## Checking whether the stack-name is passed as an arguement
if [ $# -lt 1 ]; then
  echo "Kindly provide stack name! Script execution stopped."
  exit 1
fi

#Replacing the STACK_NAME passed by the user in the csye6225-cf-networking-parameters.json
#sed -i "s/REPLACE_STACK_NAME/$1" csye6225-cf-networking-parameters.json
sed -i "0,/REPLACE_STACK_NAME/s/REPLACE_STACK_NAME/$1/" csye6225-cf-networking-parameters.json

##Creating Stack
echo "Creating Cloud Stack $1"
response=$(aws cloudformation create-stack --stack-name "$1" --template-body file://csye6225-cf-networking.json --parameters file://csye6225-cf-networking-parameters.json)
echo $?
echo "Waiting for Stack $1 to be created"
echo "$response"

aws cloudformation wait stack-create-complete --stack-name $1
echo "stack $1 created successfully"
#sed -i "s/$1/REPLACE_STACK_NAME" csye6225-cf-networking-parameters.json
sed -i "0,/$1/s/$1/REPLACE_STACK_NAME/" csye6225-cf-networking-parameters.json
