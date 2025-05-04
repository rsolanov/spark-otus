from pyspark.sql import SparkSession

spark = SparkSession.builder \
    .appName("SimplePySparkApp") \
    .master("spark://spark-master:7077") \
    .getOrCreate()

data = [("Java", 20000), ("Python", 100000), ("Scala", 3000)]
columns = ["Language", "Users"]

df = spark.createDataFrame(data, schema=columns)

df.show()

df.groupBy("Language").count().show()

spark.stop()