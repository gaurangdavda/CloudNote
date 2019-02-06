#!/bin/sh
#script to delete AWS stack
echo " "
echo "----Deleting Stack----"
echo " "
echo " "
#Getting stack name from user 
echo "Enter the name for the stack"
read stack_name
aws cloudformation delete-stack --stack-name $stack_name
