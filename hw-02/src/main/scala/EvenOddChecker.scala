object EvenOddChecker {
  def main(args: Array[String]): Unit = {
    println("Введите число:")
    val number = scala.io.StdIn.readInt()
    val result = if (number % 2 == 0) "четное" else "нечетное"
    println(s"Число $number является $result")
  }
}