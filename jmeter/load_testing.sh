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
    rm -rf $reportdir
fi

echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
read -p "Enter the domain name: " hostName

if [[ -z "$hostName" ]]; then
  echo "Kindly provide domain name"
  echo "Exiting"
  echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
  exit 1
fi

echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
read -p "Enter the attachment path: " filepath

if [[ -z "$filepath" ]]; then
  echo "Kindly provide file path"
  echo "Exiting"
  echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
  exit 1
else
  if [ -f $filepath ] ; then
    echo "Attaching $filepath to all the notes"
  else
    echo "Enter a valid attachment"
    echo "Exiting"
    echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
    exit 1
  fi
fi

function getext() {
   [ "$#" != 1 ] && { echo "Wrong number of arguments. Provide exactly one." >&2; return 254; }
   [ -r "$1" ] || { echo "Not a file, nonexistent or unreadable." >&2; return 1; }
   grep "^$(file -b --mime-type "$1")"$'\t' /etc/mime.types |
      awk -F '\t+' '{print $1}'
}
mimetype=$(getext $filepath)
echo "$mimetype is mime type of file"

echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
echo "Running JMeter with domain name: $hostName"
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
jmeter -Jhostname=$hostName -Jfilepath=$filepath -Jmimetype=$mimetype -n -t load_testing.jmx -l $filejtl -j $filelog -e -o $reportdir
