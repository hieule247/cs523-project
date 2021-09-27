package cs523.sparksql;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class DataFramDSL {

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
	
	private static void dataFrameDLS(SparkSession spark){
		 Dataset<Row> df = spark.read().table("world_total_case_report_by_country");
		 df.select("province_state").where(df.col("country_region").endsWith("US"))
		 		.filter(df.col("confirmed").gt(Integer.valueOf(1000)))
		 		.orderBy(df.col("confirmed")).limit(10).show();
	}
	
	private static void dataFrameSQL(SparkSession spark){
		 Dataset<Row> df = spark.sql("SELECT province_state from world_total_case_report_by_country "
		 		+ "where country_region = \"US\" AND confirmed > 1000 "
		 		+ "order by confirmed limit 10 ");
		 df.show();
	}
	
	private static void createAtable(SparkSession spark){
		 Dataset<Row> df = spark.read().table("world_total_case_report_by_country");
		 df.select("province_state").where(df.col("country_region").endsWith("US"))
		 		.filter(df.col("confirmed").gt(Integer.valueOf(1000)))
		 		.orderBy(df.col("confirmed")).limit(10).show();
		 df.write().mode("overwrite").saveAsTable("region_table");
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		SparkSession sparkSession = getSparkSession();
		//DataFramDSL.dataFrameDLS(sparkSession);
		//DataFramDSL.dataFrameSQL(sparkSession);
		DataFramDSL.createAtable(sparkSession);
	}
}
