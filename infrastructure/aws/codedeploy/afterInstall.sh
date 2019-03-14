#!/bin/bash
#Commands to run after after installation
echo "Entered after install hook"
pwd
ls -al
nohup java -Dspring.profiles.active=dev -jar cloud-ninja-SNAPSHOT.jar &
echo "Leaving after install hook"
