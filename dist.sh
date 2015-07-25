#!/bin/bash
rm -rf package
sbt clean compile dist
unzip target/universal/50hacks-1.0-SNAPSHOT.zip 
mv 50hacks-1.0-SNAPSHOT package 
ssh hackathon 'sudo service hackserver stop'
rsync -avz --delete package hackathon:/home/ubuntu/
scp hackserver.conf hackathon:/home/ubuntu
ssh hackathon 'sudo mv /home/ubuntu/hackserver.conf /etc/init' 
ssh hackathon 'sudo service hackserver start'

