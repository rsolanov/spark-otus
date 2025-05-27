import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object SparkDataFrameAPI {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("SparkDataFrameAPI")
      .master("local[*]")
      .config("spark.sql.adaptive.enabled", "true")
      .config("spark.sql.adaptive.coalescePartitions.enabled", "true")
      .getOrCreate()

    import spark.implicits._
    try {
      val countriesDF = spark.read.option("multiLine", value = true).json("countries.json")

      countriesDF.cache()

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

      saveDataFrame(bordersDF, "countries_with_many_borders.parquet")
      saveDataFrame(languagesDF, "language_ranking.parquet")

    } catch {
      case e: Exception =>
        println(s"Ошибка выполнения: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      spark.stop()
    }
  }

  def getCountriesWithManyBorders(df: DataFrame, minBorders: Int = 5): DataFrame = {
    df.filter(col("borders").isNotNull && size(col("borders")) >= minBorders)
      .select(
        col("name.common").alias("Country"),
        size(col("borders")).alias("NumBorders"),
        concat_ws(", ", col("borders")).alias("BorderCountries")
      )
      .orderBy(col("NumBorders").desc)
  }

  def getLanguageRanking(df: DataFrame): DataFrame = {
    val languagesExploded = df
      .filter(col("languages").isNotNull)
      .select(
        col("name.common").alias("Country"),
        from_json(to_json(col("languages")), MapType(StringType, StringType)).alias("languagesMap")
      )
      .select( 
        col("Country"),
        explode(col("languagesMap")).as(Seq("languageCode", "languageName"))
      )
      .filter(col("languageName").isNotNull && col("languageName") =!= "")

    languagesExploded
      .groupBy("languageName")
      .agg(
        count("Country").alias("NumCountries"),
        collect_list("Country").alias("Countries")
      )
      .orderBy(col("NumCountries").desc)
  }

  private def saveDataFrame(df: DataFrame, path: String): Unit = {
    try {
      df.coalesce(1)
        .write
        .mode("overwrite")
        .option("compression", "snappy")
        .parquet(path)
      println(s"Данные успешно сохранены в $path")
    } catch {
      case e: Exception =>
        println(s"Ошибка сохранения в $path: ${e.getMessage}")
    }
  }
}