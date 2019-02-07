Pre-requisites

*AWS-CLI installed
*jq to run the shell scripts to create and tear down

Cloud Formation

csye6225-aws-cf-create-stack.sh 
The script takes stack name and uses the json parameters and networking parameter file to to create the resources it needs. The script will create a VPC,Route Table,Internet Gateway which will be attachedto VPC

To run : ./csye6225-aws-cf-create-stack.sh <stackname>

csye6225-aws-cf-terminate-stack.sh 
The script takes stack name which you want to delete and tears down all the resources attached to the stack provided. The script will tear down a VPC,Route Table,Internet Gateway which will be attached to VPC

To run : ./csye6225-aws-cf-terminate-stack.sh <stackname>

csye6225-aws-networking-setup.sh

Script to create and configure required networking resources using AWS CLI

To run : ./csye6225-aws-networking-setup.sh pass all required parameters

csye6225-aws-networking-teardown.sh

script csye6225-aws-networking-teardown.sh to delete networking resources using AWS CLI

To run : ./csye6225-aws-networking-teardown.sh



