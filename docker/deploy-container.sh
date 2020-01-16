#!/bin/bash

#copy docker stack file to manager
docker-machine scp swarm.yml manager1:~


#deploy docker container
docker-machine ssh manager1 '

docker stack deploy -c swarm.yml shopdemo

'
