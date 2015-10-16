#!/bin/bash
echo url=jdbc:postgresql://$PGSQL_PORT_5432_TCP_ADDR:$PGSQL_PORT_5432_TCP_PORT/artifactory >> $ARTIFACTORY_HOME/etc/storage.properties
$ARTIFACTORY_HOME/bin/artifactory.sh
