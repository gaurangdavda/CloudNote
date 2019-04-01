#!/bin/sh
#script to delete AWS stack

set -e
if [ $# -lt 1 ]; then
  echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
  echo "Kindly provide stack name! Script execution stopped."
  echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
  exit 1
fi

echo " "
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "Deleting cloud stack with name: $1"
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo " "

stackList=$(aws cloudformation list-stacks --query 'StackSummaries[?StackStatus != `DELETE_COMPLETE`].{StackName:StackName}')
#echo "stacklist is $stackList"

if [ ! `echo $stackList | grep -w -c $1 ` -gt 0 ]
then
  echo "Stack with name: $1 does not exists"
  echo "Stack deletion failed"
  echo "Exiting.."
  exit 1
fi


aws cloudformation delete-stack --stack-name $1
echo "Stack deletion triggered"
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "Waiting for the stack $1 to be deleted"
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
aws cloudformation wait stack-delete-complete --stack-name $1
echo "Stack $1 deleted successfully"
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
