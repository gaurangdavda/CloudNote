#!/bin/bash

f () {
    errcode=$? # save the exit code as the first thing done in the trap function
    echo "error $errcode"
    echo "the command executing at the time of the error was"
    echo "$BASH_COMMAND"
    echo "on line ${BASH_LINENO[0]}"
    echo "Exiting the script."
    exit $errcode
}

trap f ERR

echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "Enter the CircleCI stack name"
read circleCIStackName
#Calling the csye6225-aws-cf-circeci.sh to create the networking stack
./csye6225-aws-cf-circeci.sh $circleCIStackName

echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "Enter the networking stack name"
read nwStackName
#Calling the csye6225-aws-cf-create-stack.sh to create the networking stack
./csye6225-aws-cf-create-stack.sh $nwStackName

echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "Enter the Application stack name"
read appStackName
#Calling the csye6225-aws-cf-create-stack.sh to create the networking stack
./csye6225-aws-cf-create-application-stack.sh $appStackName

echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "All stack creations completed successfully"
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
