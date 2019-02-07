#!/bin/sh
#script to delete AWS stack

set -e
if [ $# -lt 1 ]; then
  echo "Kindly provide stack name! Script execution stopped."
  exit 1
fi

#Replacing the STACK_NAME passed by the user in the csye6225-cf-networking-parameters.json
sed -i "s/REPLACE_STACK_NAME/$1/g" csye6225-cf-networking-parameters.json

echo " "
echo "----Deleting Stack----"
echo " "
echo " "
aws cloudformation delete-stack --stack-name $1
echo "----Waiting for the stack $1 to be deleted----"
aws cloudformation wait stack-delete-complete --stack-name $1
echo "stack $1 deleted successfully"