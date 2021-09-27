package cs523.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseTable {
	public static final String TABLE_NAME = "cs523";
	
	public static final String CF_DAILY = "daily";
	
	public static final String C_NO = "no";
	public static final String C_NEWS_DATE = "NEWS_DATE";
	public static final String C_NEWS_TIME = "NEWS_TIME";
	public static final String C_CURRENCY = "CURRENCY"  ;
	public static final String C_IMPACT = "IMPACT";
	public static final String C_DESCRIPTION = "DESCRIPTION";
	public static final String C_ACTUAL = "ACTUAL";
	public static final String C_FORECAST = "FORECAST";
	public static final String C_PREVIOUS = "_PREVIOUS";
	public static final String C_REVISED_FROM = "REVISED_FROM";
	public static final String C_EVENT_ID = "EVENT_ID";

	private static Configuration _config;
	private static Connection _connection;
	private static TableName _tableName;
	//private static long _rowKey = 0;
		
	private static void setupDB() throws IOException  {
		_config = HBaseConfiguration.create();
		_connection = ConnectionFactory.createConnection(_config);
		_tableName = TableName.valueOf(TABLE_NAME);
	}
	
	public static void createTable() throws IOException {
		setupDB();
		
		try (Admin admin = _connection.getAdmin())
		{
			HTableDescriptor table = new HTableDescriptor(_tableName);
			table.addFamily(new HColumnDescriptor(TABLE_NAME).setCompressionType(Algorithm.NONE));
			table.addFamily(new HColumnDescriptor(CF_DAILY).setCompressionType(Algorithm.NONE));

			if (!admin.tableExists(table.getTableName())) {
				System.out.print("Creating table...");
				admin.createTable(table);
			}	
			
			System.out.println("Done!");
		}		
	}
	
	public static void addData(List<String> values) throws IOException {
		Table table = _connection.getTable(_tableName);
		List<Put> puts = new ArrayList<>();
		
		Put p = new Put(Bytes.toBytes(String.valueOf(values.get(1))));
		int idx = -1;
		p.addColumn(Bytes.toBytes(CF_DAILY), Bytes.toBytes(C_NO),Bytes.toBytes(values.get(++idx)));
//		p.addColumn(Bytes.toBytes(CF_DAILY), Bytes.toBytes(C_NEWS_DATE),Bytes.toBytes(values.get(++idx)));
//		p.addColumn(Bytes.toBytes(CF_DAILY), Bytes.toBytes(C_NEWS_TIME),Bytes.toBytes(values.get(++idx)));
//		p.addColumn(Bytes.toBytes(CF_DAILY), Bytes.toBytes(C_CURRENCY),Bytes.toBytes(values.get(++idx)));
//		p.addColumn(Bytes.toBytes(CF_DAILY), Bytes.toBytes(C_IMPACT),Bytes.toBytes(values.get(++idx)));
//		p.addColumn(Bytes.toBytes(CF_DAILY), Bytes.toBytes(C_DESCRIPTION),Bytes.toBytes(values.get(++idx)));
//		p.addColumn(Bytes.toBytes(CF_DAILY), Bytes.toBytes(C_ACTUAL),Bytes.toBytes(values.get(++idx)));
//		p.addColumn(Bytes.toBytes(CF_DAILY), Bytes.toBytes(C_FORECAST),Bytes.toBytes(values.get(++idx)));
//		p.addColumn(Bytes.toBytes(CF_DAILY), Bytes.toBytes(C_PREVIOUS),Bytes.toBytes(values.get(++idx)));
//		p.addColumn(Bytes.toBytes(CF_DAILY), Bytes.toBytes(C_REVISED_FROM),Bytes.toBytes(values.get(++idx)));
//		p.addColumn(Bytes.toBytes(CF_DAILY), Bytes.toBytes(C_EVENT_ID),Bytes.toBytes(values.get(++idx)));
		
		puts.add(p);
		
		table.put(puts);
		table.close();
		
		System.out.println("Added data <rowKey>: " + values.get(0) + " successfully");
	}

}
