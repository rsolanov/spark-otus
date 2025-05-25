package example
object StringConcatenator {
  def main(args: Array[String]): Unit = {
    val words = List("Scala", "является", "мощным", "языком", "программирования")
    println("Объединенная строка: " + words.mkString(" "))
  }
}