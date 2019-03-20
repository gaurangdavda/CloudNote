#!/bin/bash
#Commands to run after after installation
echo "Entered after install hook"
cd /home/centos/webapp
sudo chown -R centos:centos /home/centos/webapp/*
sudo chmod +x cloud-ninja-SNAPSHOT.jar
kill -9 $(ps -ef|grep cloud-ninja | grep -v grep | awk '{print $2}')
source /etc/profile.d/envvariable.sh
nohup java -jar cloud-ninja-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &
#cd /opt/aws/amazon-cloudwatch-agent/etc
#sudo cp /home/centos/webapp/amazon-cloudwatch-agent-schema.json amazon-cloudwatch-agent-schema.json
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/centos/webapp/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent-schema.json -s
#sudo systemctl restart amazon-cloudwatch-agent.service
echo "Leaving after install hook"
