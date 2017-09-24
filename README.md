# Microservice Shop Demo
This is a sample microservice shop application implemented with spring boot.
It should be serve as framework for further implementations.

## Table of contents

*  [Preconditions](#preconditions)
*  [Deployment](#deployment)
*  [Run the Application](#run-the-application)
*  [Run the Application in Docker-Swarm](#run-the-application-in-docker-swarm)


## Preconditions
Before starting please install the following software.

 * Jave JDK 1.8
 * [Git](https://git-scm.com/downloads)
 * [Gradle](https://gradle.org/install/)
 * [Docker](https://www.docker.com/community-edition#/download)
 * Eclipse with the following plugins
 	* Spring IDE
 	* Eclipse Buildship
 

## Deployment
<b>Clone the the required git repositories.</b><br />

```bash
$ git clone https://github.com/maltorpro/microservice-infrastructure.git
$ git clone https://github.com/maltorpro/microservice-monitoring.git
$ git clone https://github.com/maltorpro/config-shop-demo.git
$ git clone https://github.com/maltorpro/microservice-shop-demo.git
```

<b>Build the projects using gradle over the console.</b>

```bash
$ cd microservice-infrastructure
$ ./gradlew build

$ cd microservice-monitoring
$ ./gradlew build

$ cd /microservice-shop-demo
$ ./gradlew build
```

<b>Setup the Eclipse workspace.</b><br />
Import the projects using the import "Existing Gradle Project" provided by the Eclipse-Buildship plugin.
You can build the projects by clicking on "Refresh Gradle Project" in the context menu of the project. 

## Run the Application in Eclipse
Open the "Boot Dashboard" view and start the applications in the the following order:<br />
<b>Infrastructure</b>
1. config-server
2. discover-server
3. edge-server
4. auth-server
5. resource-server

<b>Services</b><br />
1. product-service
2. recommendation-service
3. review-service
4. product-composite-service

<b>Monitoring</b><br />
Since further docker images are required for the monitoring, docker swarm is preferred for execution.

## Run the Application in Docker-Swarm
The docker images are pushed to my docker hub repository. <br />
[docker repository](https://hub.docker.com/u/maltor/)

You can use this images for creation a docker swarm.
For more information, please see the following notes:<br />
[docker swarm notes](https://github.com/maltorpro/microservice-shop-demo/blob/shop/docker/docker%20swarm%20tut.txt)<br />
[access URLs](https://github.com/maltorpro/microservice-shop-demo/blob/shop/notes)<br />
[apache2 config](https://github.com/maltorpro/microservice-shop-demo/blob/shop/000-default.conf)

Of course, you can also build your own docker images. Therefore, the required docker files are available in each project.

