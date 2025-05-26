import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object SparkDataFrameAPI {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("SparkDataFrameAPI")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val countriesDF = spark.read.json("countries.json")

    val bordersDF = getCountriesWithManyBorders(countriesDF)
    bordersDF.write.parquet("countries_with_many_borders.parquet")

    val languagesDF = getLanguageRanking(countriesDF)
    languagesDF.write.parquet("language_ranking.parquet")

    spark.stop()
  }

  def getCountriesWithManyBorders(countriesDF: DataFrame): DataFrame = {
    countriesDF
      .filter(size(col("borders")) >= 5)
      .select(
        col("name.common").alias("Country"),
        size(col("borders")).alias("NumBorders"),
        concat_ws(", ", col("borders")).alias("BorderCountries")
      )
      .orderBy(col("NumBorders").desc)
  }

  def getLanguageRanking(countriesDF: DataFrame): DataFrame = {
    val explodedDF = countriesDF
      .select(
        col("name.common").alias("Country"),
        explode(map_keys(col("languages"))).alias("Language")
      )

    explodedDF
      .groupBy("Language")
      .agg(
        count("Country").alias("NumCountries"),
        collect_list("Country").alias("Countries")
      )
      .orderBy(col("NumCountries").desc)
  }
}