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

## Build Instructions
* Build the project using Maven
* Start the application from an IDE of choice or the command line
* Spin up the PostgreSQL DB(sudo -u postgres psql)
* Create a database with the name "webappdb"
* Create an user "webappusr" and grant all permissions on webappdb schema

## DNS Setup 

Register a domain name with Namecheap as your registrar
Domain name setup with the format csye6225-spring2019-husky_id.me
Create a public hosted zone in Amazon Route 53
Configure Namecheap to use custom nameservers provided by Amazon Route 53
Create a type TXT record for your domain with TTL of 10 seconds 
Type TXT recordshould contains the text value "csye6225-spring2019"

## Deploy Instructions
Application is deployed locally and runs on http://localhost:8080/

## Running Tests
The unit tests can be found under the test package. The unit tests are developedusing Junit4

Install Postman
Demo of all the API implementation on Postman 

## CI/CD


