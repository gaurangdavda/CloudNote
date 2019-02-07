#!/bin/sh
#script to delete AWS stack

set -e
if [ $# -lt 1 ]; then
  echo "Kindly provide stack name! Script execution stopped."
  exit 1
fi

echo " "
echo "----Deleting Stack----"
echo " "
echo " "
aws cloudformation delete-stack --stack-name $1
echo "----Waiting for the stack $1 to be deleted----"
aws cloudformation wait stack-delete-complete --stack-name $1
echo "stack $1 deleted successfully"