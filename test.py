from fabric import *
import json
import requests

def send_test_alarm(host="52.74.9.6", port="80"):
    data = {"state" : 0 , "location" : { "latitude" : 123.12,"longitude" : 345.123}} 
    response = requests.post("http://"+host+":"+port+"/api/users/testuser/events/alarms", json=data)
    print(response.content)

def get_test_alarms(host="52.74.9.6", port="80"):
    response = requests.get("http://"+host+":"+port+"/api/users/testuser/events/alarms")
    print(response.content)
    

