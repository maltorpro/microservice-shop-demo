#!/bin/bash

openssl x509 -enddate -noout -in maltor-certificate/combined.pem

if openssl x509 -checkend 86400 -noout -in maltor-certificate/combined.pem
then
	echo "Certificate is good for another day!"
else
	echo "Certificate has expired or will do so within 24 hours!"
	echo "(or is invalid/not found)"
fi


