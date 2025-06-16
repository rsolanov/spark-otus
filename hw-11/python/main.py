from pyspark.sql import SparkSession, DataFrame
from pyspark.sql.functions import col, size, concat_ws


def get_spark():
    return SparkSession.builder \
        .appName("SparkDataFrameAPI") \
        .master("local[*]") \
        .config("spark.sql.adaptive.enabled", "true") \
        .config("spark.sql.adaptive.coalescePartitions.enabled", "true") \
        .config("spark.sql.debug.maxToStringFields", "1000") \
        .config("spark.sql.log.level", "ERROR") \
        .getOrCreate()


def get_countries_with_many_borders(df: DataFrame, min_borders: int = 5) -> DataFrame:
    return (
        df.filter((col("borders").isNotNull()) & (size(col("borders")) >= min_borders))
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
        countries_df = sp.read.option("multiLine", value=True).json("countries.json")

        countries_df.cache()

        print("Схема данных:")
        countries_df.printSchema()

        print("Пример данных:")
        countries_df.select("name.common", "borders").show(5, truncate=False)

        borders_df = get_countries_with_many_borders(countries_df)
        print("Страны с 5+ границами:")

        borders_df.show(5, False)
    except Exception as e:
        print(str(e))
    finally:
        sp.stop()


if __name__ == "__main__":
    main()