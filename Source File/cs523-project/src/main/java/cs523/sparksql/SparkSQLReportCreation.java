package cs523.sparksql;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSQLReportCreation {
	
	private static SparkSession getSparkSession() {
		SparkSession spark = SparkSession
				  .builder()
				  .appName("Java Spark SQL basic example")
				  .master("local[2]")
				  .config(SQLQuery.SPARK_SQL_WAREHOUSE_DIR, SQLQuery.SPARK_SQL_WAREHOUSE_LOCATION) 
				          		.config(SQLQuery.HIVE_CONNECTOR_METADATA, SQLQuery.HIVE_CONNECTOR_THRIFT_URL) 

				          		.enableHiveSupport()
				  .getOrCreate();
		return spark;
	}
	
	private static void processData(SparkSession spark) throws IOException, ClassNotFoundException, SQLException {
		Dataset<Row> sqlDF = spark.sql(SQLQuery.US_ACTIVE_CASE_REPORT_BY_STATE);
		writeReport(spark, sqlDF, "US_ACTIVE_CASE_REPORT_BY_STATE");
		
		sqlDF = spark.sql(SQLQuery.US_ACTIVE_CASE_REPORT_BY_MONTH);
		writeReport(spark, sqlDF, "US_ACTIVE_CASE_REPORT_BY_MONTH");
		
		sqlDF = spark.sql(SQLQuery.WORLD_TOTAL_CASE_REPORT_BY_COUNTRY);
		writeReport(spark, sqlDF, "WORLD_TOTAL_CASE_REPORT_BY_COUNTRY");
	}

	private static void writeReport(SparkSession spark, Dataset<Row> sqlDF, String tableName) {
		sqlDF.write().mode("overwrite").saveAsTable(tableName);
	}
	
	private static void dataFrameDLS(SparkSession spark){
		 Dataset<Row> df = spark.read().table("world_total_case_report_by_country");
		 df.select("country_region").show();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		try {
			SparkSQLReportCreation.processData( getSparkSession());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}