from fabric import *
import json
import requests

def send_test_alarm(host="52.74.9.6", port="80", alarm="fall"):
    data = {"state" : 0 , "location" : { "latitude" : 1.3,"longitude" : 103.8}} 
    response = requests.post("http://"+host+":"+port+"/api/users/testuser/events/"+alarm, json=data)
    print(response.content)

def send_heartrate(host="52.74.9.6", port="80", rate=100):
    data = {"bpm" : int(rate) , "location" : { "latitude" : 1.3,"longitude" : 103.8}} 
    response = requests.post("http://"+host+":"+port+"/api/users/testuser/events/heartrate", json=data)
    print(response.content)

def get_test_alarms(host="52.74.9.6", port="80", alarm="fall"):
    response = requests.get("http://"+host+":"+port+"/api/users/testuser/events/"+alarm)
    print(response.content)
    

