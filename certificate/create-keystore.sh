#!/bin/bash

mkdir -p maltor-certificate
cd maltor-certificate
rm -f combined.pem
rm -f cert.p12

sudo cat /etc/letsencrypt/live/maltor.de/cert.pem /etc/letsencrypt/live/maltor.de/privkey.pem > combined.pem 
openssl pkcs12 -export -in combined.pem -out cert.p12


