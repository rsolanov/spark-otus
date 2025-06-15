from pyspark.sql import SparkSession, DataFrame
from pyspark.sql.functions import col, size, concat_ws


def get_spark():
    return SparkSession.builder \
    .appName("SparkDataFrameAPI") \
    .master("local[*]") \
    .config("spark.sql.adaptive.enabled", "true") \
    .config("spark.sql.adaptive.coalescePartitions.enabled", "true") \
    .getOrCreate()

def getCountriesWithManyBorders(df: DataFrame, minBorders: int = 5) -> DataFrame:
    return (
        df.filter((col("borders").isNotNull()) & (size(col("borders")) >= minBorders))
        .select(
            col("name.common").alias("Country"),
            size(col("borders")).alias("NumBorders"),
            concat_ws(", ", col("borders")).alias("BorderCountries")
        )
        .orderBy(col("NumBorders").desc())
    )

def main():
    sp = get_spark()
    try:
        countriesDF = sp.read.option("multiLine", value = True).json("countries.json")

        countriesDF.cache()

        print("Схема данных:")
        countriesDF.printSchema()

        print("Пример данных:")
        countriesDF.select("name.common", "borders").show(5, truncate = False)

        bordersDF = getCountriesWithManyBorders(countriesDF)
        print("Страны с 5+ границами:")

        bordersDF.show(5, False)
    except Exception as e:
        print(str(e))
    finally:
        sp.stop()

if __name__ == "__main__":
    main()
