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
for key in `aws ec2 describe-key-pairs --output text | cut -f3`
do
  echo -e $key
done
echo "Choose 1 Key which you want to use!"
read KEY_CHOSEN

echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"

echo "Displaying AMI!"
for image in `aws ec2 describe-images --owners self --query 'Images[*].{ID:ImageId}' --output text | cut -f1`
do
  echo -e $image
done
echo "Enter AMI ID"
read amiId
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"


echo "Displaying Certificates"
for certificate in `aws acm list-certificates --query 'CertificateSummaryList[*].{ID:CertificateArn}' --output text | cut -f1`
do
  echo -e $certificate
done
echo "Enter Certificate ARN"
read certificate_arn
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"

echo "Displaying HostedZones Domains"
for hostedzone in `aws route53 list-hosted-zones --query HostedZones[0].Name --output text`
do
  echo -e ${hostedzone::-1}
done
echo "Enter HostedZoneID"
read hostedZone
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"

echo "Displaying HostedZones ID"
for hostedzoneid in `aws route53 list-hosted-zones --query 'HostedZones[0].Id' --output text`
do
  echo -e ${hostedzoneid#*e/}
done
echo "Enter HostedZoneID"
read hostedZoneId
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"

stackList=$(aws cloudformation list-stacks --query 'StackSummaries[?StackStatus != `DELETE_COMPLETE`].{StackName:StackName}')

if [  `echo $stackList | grep -w -c $1 ` -gt 0 ]
then
  echo "Stack with name: $1  exists"
  echo "Stack creation failed"
  echo "Exiting.."
  exit 1
fi
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
#Listing the buckets in the AWS account and allowing the user to pick the codeDeploy bucket
echo "Displaying all buckets in the AWS Account"
aws s3 ls --human-readable
echo "Select the S3 bucket to store codedeploy artifacts via CircleCI"
read s3BucketName

if [ -z s3BucketName ]
    then
    echo "Bucket name not provided, exiting code"
    exit 1
fi

echo "Selected bucket : $s3BucketName"
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"

#Listing the buckets in the AWS account and allowing the user to pick the bucket for saving attachments
echo "Displaying all buckets in the AWS Account"
aws s3 ls --human-readable
echo "Select the S3 bucket to store attachments of notes"
read s3BucketNameForWebApp

if [ -z s3BucketNameForWebApp ]
    then
    echo "Bucket name not provided, exiting code"
    exit 1
fi

echo "Selected bucket : $s3BucketNameForWebApp"
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"

##Creating WAF Stack
response_waf=$(aws cloudformation create-stack --stack-name waf-rules --template-body file://owasp_10_base.yml)
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "Waiting for Stack waf-rules to be created"
echo "$response_waf"

aws cloudformation wait stack-create-complete --stack-name waf-rules
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "stack waf-rules created successfully"


##Creating Stack
#echo "Creating Cloud Stack $1"
response=$(aws cloudformation create-stack --stack-name "$1" --template-body file://csye6225-cf-auto-scaling-application.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey="KEYNAME",ParameterValue=$KEY_CHOSEN ParameterKey="AMIID",ParameterValue=$amiId ParameterKey="BUCKETNAME",ParameterValue=$s3BucketName ParameterKey="APPNAME",ParameterValue="csye6225-webapp" ParameterKey="DEPGROUPNAME",ParameterValue="csye6225-webapp-deployment" ParameterKey="BUCKETNAMEFORWEBAPP",ParameterValue=$s3BucketNameForWebApp ParameterKey="CERTIFICATEARN",ParameterValue=$certificate_arn ParameterKey="HOSTEDZONE",ParameterValue=$hostedZone ParameterKey="HOSTEDZONEID",ParameterValue=$hostedZoneId)
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "Waiting for Stack $1 to be created"
echo "$response"

aws cloudformation wait stack-create-complete --stack-name $1
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "stack $1 created successfully"
