package cs523.sparksql;

public class SQLQuery {
	public static final String SPARK_SQL_WAREHOUSE_DIR = "spark.sql.warehouse.dir";
	public static final String SPARK_SQL_WAREHOUSE_LOCATION = "/user/hive/warehouse";
	public static final String HIVE_CONNECTOR_METADATA = "hive.metastore.uris"; 
	public static final String HIVE_CONNECTOR_THRIFT_URL = "thrift://quickstart.cloudera:9083";
	
	public static final String US_ACTIVE_CASE_REPORT_BY_STATE = "select province_state, MAX((cast(confirmed as int) - cast(deaths as int) - cast(recovered as int)))"
			+ " as active, "
			+ "MAX((cast(confirmed as int))) as confirmed, "
			+ "MAX((cast(deaths as int))) as deaths, "
			+ "MAX((cast(recovered as int))) as recovered "
			+" FROM covid19 WHERE country_region = 'US' "
			+ " AND date_format(cast(to_date(from_unixtime(unix_timestamp(observation_date, 'MM/dd/yyyy'))) as date),'yyyyMMdd') = '20210205'"
			+ " group by province_state order by active";
	public static final String US_ACTIVE_CASE_REPORT_BY_MONTH = "select country_region,province_state,deaths, recovered,"
			+ " cast(to_date(from_unixtime(unix_timestamp(observation_date, 'MM/dd/yyyy'))) as date) as recordDate, "
+ " (cast(confirmed as int) - cast(deaths as int) - cast(recovered as int)) as active, "
+ " date_format(cast(to_date(from_unixtime(unix_timestamp(observation_date, 'MM/dd/yyyy'))) as date),'yyyy-MM') as month, "
+ " date_format(cast(to_date(from_unixtime(unix_timestamp(observation_date, 'MM/dd/yyyy'))) as date),'yyyy') as year "
+ " from covid19 where country_region = 'US' "
+ " order by cast(to_date(from_unixtime(unix_timestamp(observation_date, 'MM/dd/yyyy'))) as date)";

	public static final String WORLD_TOTAL_CASE_REPORT_BY_COUNTRY = "select country_region, province_state, "
			+ "MAX((cast(confirmed as int))) as confirmed, "
			+ "MAX((cast(deaths as int))) as deaths, "
			+ "MAX((cast(recovered as int))) as recovered "
			+ " from covid19 group by country_region, province_state order by country_region";
}
