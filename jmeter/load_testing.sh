#!/bin/bash

set -e

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

filejtl="access.jtl"
filelog="jmeter.log"
reportdir="report"

if [ -f $filejtl ] ; then
    rm $filejtl
fi

if [ -f $filelog ] ; then
    rm $filelog
fi

if [ -d $reportdir ] ; then
    rm -RF $reportdir
fi

## Checking whether the domain name is passed as an arguement
if [ $# -lt 1 ]; then
  echo "Kindly provide domain name! Script execution stopped."
  exit 1
fi
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "Running JMeter with domain name: $1"
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
jmeter -Jhostname=$1 -n -t load_testing.jmx -l $filejtl -j $filelog -e -o $reportdir
