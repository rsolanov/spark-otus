import org.apache.spark.{SparkConf, SparkContext}
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import scala.util.Try

object RDDAPI {
  def main(args: Array[String]): Unit = {
    val conf = {
      new SparkConf().setAppName("RDDAPI").setMaster("local[*]")
    }
    val sc = new SparkContext(conf)

    try {
      val tripData = sc.textFile("tripdata.csv")
        .filter(!_.startsWith("VendorID"))
        .map { line =>
          val fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)
          Try {
            val pickupLocationId = fields(7).replace("\"", "").toInt
            val pickupDateTime = LocalDateTime.parse(fields(1).replace("\"", ""), DateTimeFormatter.ISO_DATE_TIME)
            (pickupLocationId, pickupDateTime)
          }.getOrElse((0, LocalDateTime.MIN))
        }
        .filter(_._1 != 0)

      val zoneData = sc.textFile("taxi_zone_lookup.csv")
        .filter(!_.startsWith("\"LocationID\""))
        .map { line =>
          val fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)
          Try {
            val locationId = fields(0).replace("\"", "").toInt
            val zone = fields(2).replace("\"", "")
            (locationId, zone)
          }.getOrElse((0, "Unknown"))
        }
        .filter(_._1 != 0)

      val joinedData = tripData.join(zoneData)

      val hourZonePairs = joinedData.map {
        case (_, (datetime, zone)) =>
          ((datetime.getHour, zone), 1)
      }

      val tripCounts = hourZonePairs.reduceByKey((x, y) => x + y)

      val results = tripCounts.map {
        case ((hour, zone), count) => s"$hour,$zone,$count"
      }

      results.repartition(1).saveAsTextFile("output")

    } catch {
      case e: Exception =>
        println(s"Ошибка выполнения: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      sc.stop()
    }
  }
}