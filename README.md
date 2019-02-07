# CSYE 6225 - Spring 2019

## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
| Vivek Dalal|001430934|dalal.vi@husky.neu.edu |
|Mansi Raghuwanshi|001888977|raghuwanshi.m@husky.neu.edu |
|Gaurang Davda|001826203|davda.g@husky.neu.edu|
|Karan Barai|001832013|barai.k@husky.neu.edu|

## Technology Stack
* Java 1.8
* Spring Boot 2.1.2
* PostgresSQL
* JUnit4

##Pre-requisites
* AWS-CLI installed
* jq to run the shell scripts to create and tear down

## Build Instructions
* Build the project using Maven
* Start the application from an IDE of choice or the command line
* Spin up the PostgreSQL DB(sudo -u postgres psql)
* Create a database with the name "webappdb"
* Create an user "webappusr" and grant all permissions on webappdb schema

## Cloud Formation

* csye6225-aws-cf-create-stack.sh
The script takes stack name and uses the json parameters and networking parameter file to to create the resources it needs.
The script will create a VPC,Route Table,Internet Gateway which will be attachedto VPC

To run : ./csye6225-aws-cf-create-stack.sh <stackname> 

* csye6225-aws-cf-terminate-stack.sh
The script takes stack name which you want to delete and tears down all the resources attached to the stack provided.
The script will tear down a VPC,Route Table,Internet Gateway which will be attached to VPC

To run : ./csye6225-aws-cf-terminate-stack.sh <stackname> 

## Deploy Instructions
Application is deployed locally and runs on http://localhost:8080/

## Running Tests
The unit tests can be found under the test package. The unit tests are developed using Junit4.

## CI/CD


