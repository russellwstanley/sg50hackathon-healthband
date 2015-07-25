#!/bin/bash
rm -rf package
sbt dist
unzip target/universal/50hacks-1.0-SNAPSHOT.zip 
mv 50hacks-1.0-SNAPSHOT package 
ssh hackathon 'rm -rf /home/ubuntu/package'
scp -r package hackathon:/home/ubuntu
scp hackserver.conf /home/ubuntu
ssh hackathon 'sudo mv /home/ubuntu/hackserver.conf /etc/init' 

