#!/bin/bash

#sudo apt-get install git
#cd /opt
#sudo git clone https://github.com/letsencrypt/letsencrypt
#cd letsencrypt
#./letsencrypt-auto --help

sudo /etc/init.d/apache2 stop

sudo /opt/letsencrypt/letsencrypt-auto certonly --standalone -d domain.de -d www.domain.de

