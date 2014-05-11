#!/bin/bash

while true
do

rsync -avz "./web-interface/src/main/webapp/" "./startup/target/gfdeploy/nl.rug.search.opr_startup_ear_0.7-SNAPSHOT/web-interface-0.7-SNAPSHOT_war"

# use either "enter" to redeploy or do it every 5 seconds
read line
# sleep 5
done
