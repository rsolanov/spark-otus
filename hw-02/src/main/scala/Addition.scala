object Addition {
  def addNumbers(a: Int, b: Int): Int = {
    a + b
  }

  def main(args: Array[String]): Unit = {
    println("Введите число A:")
    val a = scala.io.StdIn.readLine().toInt
    println("Введите число B:")
    val b = scala.io.StdIn.readLine().toInt
    println("Сумма A и B: " + addNumbers(a, b).toString)
  }
}
