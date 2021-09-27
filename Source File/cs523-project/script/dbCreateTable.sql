-- Drop existing table
DROP TABLE IF EXISTS cs523;

-- create table on Hive based on Hbase
CREATE EXTERNAL TABLE cs523
 (	
	key int,
	no string,
	NEWS_DATE string,
	NEWS_TIME string,
	CURRENCY string,
	IMPACT string,
	DESCRIPTION string,
	ACTUAL decimal(10,2),
	FORECAST decimal(10,2),
	_PREVIOUS decimal(10, 2),
	REVISED_FROM string,
	EVENT_ID string
) 
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES 
("hbase.columns.mapping" = 
	":key,
	daily:no,
	daily:NEWS_DATE,
	daily:NEWS_TIME,
	daily:CURRENCY,
	daily:IMPACT,
	daily:DESCRIPTION,
	daily:ACTUAL,
	daily:FORECAST,
	daily:_PREVIOUS,
	daily:REVISED_FROM,
	daily:EVENT_ID"
)
TBLPROPERTIES ("hbase.table.name" = "cs523");
