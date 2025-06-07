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

      joinedData.take(3).foreach {
        case (id, (datetime, zone)) =>
          println(s"ID: $id, DateTime: $datetime, Zone: $zone")
      }

      val hourZonePairs = joinedData.map {
        case (_, (datetime, zone)) =>
          ((datetime.getHour, zone), 1)
      }

      val tripCounts = hourZonePairs.reduceByKey(_ + _)

      val results = tripCounts.map {
        case ((hour, zone), count) => s"$hour,$zone,$count"
      }

      results.saveAsTextFile("output/trip_counts_by_hour_and_zone")

    } catch {
      case e: Exception =>
        println(s"Ошибка выполнения: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      sc.stop()
    }
  }
}