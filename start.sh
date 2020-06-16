#!/bin/bash

/usr/share/filebeat/filebeat -c /etc/filebeat/filebeat.yml &

java -jar lib/jars/jetty-runner.jar lib/iam-login.war