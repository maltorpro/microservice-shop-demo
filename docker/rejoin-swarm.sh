#!/bin/bash

#remove all nodes from the manager
docker-machine ssh manager1 '
	
docker node rm infrastructure1
docker node rm monitoring1
docker node rm services1

'

#leave and then join the swarm
docker-machine ssh infrastructure1 '

docker swarm leave -f
docker swarm join --token SWMTKN-1-2fki06c2rggqldk3pfx3wgah4uq2vkntb8rsrgalszxq7bcubd-cwblvkjchlsgicw5avptoch1t 192.168.99.104:2377
'
docker-machine ssh monitoring1 '

docker swarm leave -f
docker swarm join --token SWMTKN-1-2fki06c2rggqldk3pfx3wgah4uq2vkntb8rsrgalszxq7bcubd-cwblvkjchlsgicw5avptoch1t 192.168.99.104:2377
'

docker-machine ssh services1 '

docker swarm leave -f
docker swarm join --token SWMTKN-1-2fki06c2rggqldk3pfx3wgah4uq2vkntb8rsrgalszxq7bcubd-cwblvkjchlsgicw5avptoch1t 192.168.99.104:2377
'


