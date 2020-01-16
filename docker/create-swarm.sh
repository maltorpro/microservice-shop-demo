#!/bin/bash

#create vms
docker-machine create --driver virtualbox  --virtualbox-memory "1024" manager1
docker-machine create --driver virtualbox  --virtualbox-memory "6144" infrastructure1
docker-machine create --driver virtualbox  --virtualbox-memory "2048" services1
docker-machine create --driver virtualbox  --virtualbox-memory "4096" monitoring1

#start the vml
VBoxManage startvm "manager1" --type headless
VBoxManage startvm "infrastructure1" --type headless
VBoxManage startvm "monitoring1" --type headless
VBoxManage startvm "services1" --type headless

#init swarm
docker-machine ssh manager1 '
	
	docker swarm init --advertise-addr 192.168.99.100

'

#Note the printed token from the preview command and compare it to the token below.

docker-machine ssh infrastructure1 '

docker swarm join --token SWMTKN-1-47bv95u3jwocopjzyqs51v4ngeytqxkg5chlu6pxhhqgjdt2e5-1d4cmlsgizpi6ieloiya6efas 192.168.99.100:2377

'
docker-machine ssh monitoring1 '

docker swarm join --token SWMTKN-1-47bv95u3jwocopjzyqs51v4ngeytqxkg5chlu6pxhhqgjdt2e5-1d4cmlsgizpi6ieloiya6efas 192.168.99.100:2377

'

docker-machine ssh services1 '

docker swarm join --token SWMTKN-1-47bv95u3jwocopjzyqs51v4ngeytqxkg5chlu6pxhhqgjdt2e5-1d4cmlsgizpi6ieloiya6efas 192.168.99.100:2377

'