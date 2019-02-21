Pre-requisites

*AWS-CLI installed
*jq to run the shell scripts to create and tear down

Cloud Formation

1.csye6225-aws-cf-create-stack.sh
The script takes stack name and uses the json parameters and networking parameter file to to create the resources it needs. The script will create a VPC,Route Table,Internet Gateway which will be attachedto VPC

To run : ./csye6225-aws-cf-create-stack.sh "yourstackname"

2.csye6225-aws-cf-terminate-stack.sh
The script takes stack name which you want to delete and tears down all the resources attached to the stack provided. The script will tear down a VPC,Route Table,Internet Gateway which will be attached to VPC

To run : ./csye6225-aws-cf-terminate-stack.sh "yourstackname"


3.csye6225-aws-cf-create-application-stack.sh The script takes stack name and uses the json parameters and networking parameter file to to create the resources it needs. The script will create a EC2,RDS,Dynamo DB which will be attachedto VPC

To run : ./csye6225-aws-cf-create-stack.sh "yourstackname"

4.csye6225-aws-cf-terminate-application-stack.sh The script takes stack name which you want to delete and tears down all the resources attached to the stack provided. The script will tear down a EC2,RDS,Dynamo DB  which will be attached to VPC

To run : ./csye6225-aws-cf-terminate-stack.sh "yourstackname"

Steps to connect to rds:

psql --host="your rds endpoint here" --port=5432 --username=csye6225master --password --dbname=csye6225

All the vm arguments are as follows:

-Dspring.profiles.active=dev -DamazonProperties.bucketName=csye6225-s19-raghuwanshim.me.csye6225.com -Dspring.datasource.url=jdbc:postgresql://csye6225-spring2019.c1edhmd1wujw.us-east-1.rds.amazonaws.com:5432/csye6225 -Dspring.datasource.username=csye6225master -Dspring.datasource.password=csye6225password


Note: When running locally please remove the VM arguments and run the application.