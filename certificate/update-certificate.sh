#!/bin/bash

sudo /opt/letsencrypt/letsencrypt-auto certonly --renew-by-default -d maltor.de -d www.maltor.de
