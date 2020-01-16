#!/bin/bash

VBoxManage controlvm "manager1" savestate
VBoxManage controlvm "infrastructure1" savestate
VBoxManage controlvm "monitoring1" savestate
VBoxManage controlvm "services1" savestate