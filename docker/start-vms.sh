#!/bin/bash

VBoxManage startvm "manager1" --type headless
VBoxManage startvm "infrastructure1" --type headless
VBoxManage startvm "monitoring1" --type headless
VBoxManage startvm "services1" --type headless