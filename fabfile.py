from fabric.api import *

def all_tests():
    local('sbt test')
