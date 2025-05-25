object StringConcatenator {
  def concatStrings(strings: List[String]): String = {
    strings.mkString(" ")
  }

  def main(args: Array[String]): Unit = {
    val words = List("Scala", "является", "мощным", "языком", "программирования")
    println("Объединенная строка: " + concatStrings(words))
  }
}