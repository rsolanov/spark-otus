import org.apache.spark.{SparkConf, SparkContext}

object RDDAPI {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("RDDAPI").setMaster("local[*]")
    val sc = new SparkContext(conf)

    try {
      val tripData  = sc.textFile("tripdata.csv")

    } catch {
      case e: Exception =>
        println(s"Ошибка выполнения: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      sc.stop()
    }
  }
}