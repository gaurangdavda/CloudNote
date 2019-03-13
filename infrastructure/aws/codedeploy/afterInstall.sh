#!/bin/bash
#Commands to run after after installation
java -jar cloud-ninja-SNAPSHOT.jar -Dspring.profiles.active=dev -DamazonProperties.bucketName=${S3_BUCKET_NAME} -Dspring.datasource.url=${DB_ENDPOINT} -Dspring.datasource.username=${DB_USERNAME} -Dspring.datasource.password=${DB_PASSWORD}
