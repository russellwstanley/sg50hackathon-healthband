#!/bin/bash
rm -rf package
sbt dist
unzip target/universal/50hacks-1.0-SNAPSHOT.zip 
mv 50hacks-1.0-SNAPSHOT package 
scp -r package hackathon:/home/ubuntu

