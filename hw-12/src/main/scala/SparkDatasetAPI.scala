import org.apache.spark.sql.{SparkSession, Dataset}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

case class Trip(
                 VendorID: Int,
                 tpep_pickup_datetime: String,
                 tpep_dropoff_datetime: String,
                 passenger_count: Int,
                 trip_distance: Double,
                 RatecodeID: Int,
                 store_and_fwd_flag: String,
                 PULocationID: Int,
                 DOLocationID: Int,
                 payment_type: Int,
                 fare_amount: Double,
                 extra: Double,
                 mta_tax: Double,
                 tip_amount: Double,
                 tolls_amount: Double,
                 improvement_surcharge: Double,
                 total_amount: Double
               )

case class Zone(
                 LocationID: Int,
                 Borough: String,
                 Zone: String,
                 service_zone: String
               )

case class ZoneStats(
                      LocationID: Int,
                      Borough: String,
                      Zone: String,
                      trip_count: Long,
                      min_distance: Double,
                      avg_distance: Double,
                      max_distance: Double,
                      stddev_distance: Double
                    )

object SparkDatasetAPI {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Taxi Trip Analysis")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val tripsDS: Dataset[Trip] = spark.read
      .parquet("yellow_taxi")
      .as[Trip]

    val zonesDS: Dataset[Zone] = spark.read
      .option("header", "true")
      .csv("taxi_zones.csv")
      .withColumn("LocationID", col("LocationID").cast(IntegerType))
      .as[Zone]

    val tripStats = tripsDS.groupBy("PULocationID")
      .agg(
        count("*").as("trip_count"),
        min("trip_distance").as("min_distance"),
        avg("trip_distance").as("avg_distance"),
        max("trip_distance").as("max_distance"),
        stddev("trip_distance").as("stddev_distance")
      )
      .withColumnRenamed("PULocationID", "LocationID")

    val result = zonesDS.join(tripStats, "LocationID")
      .select(
        $"LocationID",
        $"Borough",
        $"Zone",
        $"trip_count",
        $"min_distance",
        $"avg_distance",
        $"max_distance",
        $"stddev_distance"
      )
      .as[ZoneStats]

    result.write.parquet("taxi_zone_stats")

    spark.stop()
  }
}