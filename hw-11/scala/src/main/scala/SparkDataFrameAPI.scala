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
    try {
      val countriesDF = spark.read.option("multiLine", value = true).json("countries.json")

      println("Схема данных:")
      countriesDF.printSchema()

      println("Пример данных:")
      countriesDF.select("name.common", "borders").show(5, truncate = false)

      val bordersDF = getCountriesWithManyBorders(countriesDF)
      println("Страны с 5+ границами:")
      bordersDF.show(false)

      val languagesDF = getLanguageRanking(countriesDF)
      println("Рейтинг языков:")
      languagesDF.show(false)

      bordersDF.write.parquet("countries_with_many_borders.parquet")
      languagesDF.write.parquet("language_ranking.parquet")
    }
    finally {
      spark.stop()
    }
  }

  def getCountriesWithManyBorders(df: DataFrame): DataFrame = {
    df.filter(size(col("borders")) >= 5)  // Фильтр стран с 5+ границами
      .select(
        col("name.common").alias("Country"),
        size(col("borders")).alias("NumBorders"),
        concat_ws(", ", col("borders")).alias("BorderCountries")
      )
      .orderBy(col("NumBorders").desc)
  }

  def getLanguageRanking(df: DataFrame): DataFrame = {
    val languagesExploded = df.select(
      col("name.common").alias("Country"),
      explode(col("languages")).as(Seq("LanguageCode", "Language"))
    )

    languagesExploded
      .groupBy("Language")
      .agg(
        count("Country").alias("NumCountries"),
        collect_list("Country").alias("Countries")
      )
      .orderBy(col("NumCountries").desc)
  }
}