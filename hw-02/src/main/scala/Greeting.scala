object Greeting {
  def main(args: Array[String]): Unit = {
    println("Введите ваше имя:")
    val name = scala.io.StdIn.readLine()
    println(s"Привет, $name! Добро пожаловать в мир Scala!")
  }
}
