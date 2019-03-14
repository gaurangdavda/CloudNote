#!/bin/bash
#Commands to run after after installation
echo "Entered after install hook"
cd /home/centos/webapp
sudo chown -R centos:centos /home/centos/webapp/*
sudo chmod +x cloud-ninja-SNAPSHOT.jar
kill -9 $(ps -ef|grep cloud-ninja | grep -v grep | awk '{print $2}')
source /etc/profile.d/envvariable.sh
nohup java -Dspring.profiles.active=dev -jar cloud-ninja-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &
echo "Leaving after install hook"
