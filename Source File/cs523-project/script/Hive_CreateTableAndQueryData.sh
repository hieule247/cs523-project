#!/bin/sh

#start hbase service if it automatically stopped
echo "---> start hbase-master and hbase-regionserver"
sudo service hbase-master start | sudo service hbase-regionserver start

#create table
hiveCreateTable="/home/cloudera/workspace/cs523-project/script/dbCreateTable.sql"
#Validate table
hiveTableValidate="/home/cloudera/workspace/cs523-project/script/dbValidate.sql"

echo "---> create table on Hive based on HBase"
hive -f $dbCreateTable
echo "---> query data using Hive"
hive -f $dbValidate
