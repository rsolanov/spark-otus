object ListIncrement {
  def main(args: Array[String]): Unit = {
    val numbers = List(1, 2, 3, 4, 5)
    val incremented = numbers.map(i => i + 1)
    println("Исходный список: " + numbers)
    println("Список после увеличения: " + incremented)
  }
}