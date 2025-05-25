object hw04 {
  def main(args: Array[String]): Unit = {
    val age: Int = 44
    val weight: Double = 98
    val name: String = "Роман"
    val isStudent: Boolean = true

    println(s"Имя: $name")
    println(s"Возраст: $age")
    println(s"Вес: $weight")
    println(s"Студент: $isStudent")

    def sum(a: Int, b: Int): Int = a + b

    val a = 5
    val b = 7
    val sumResult = sum(a, b)
    println(s"Сумма $a и $b: $sumResult")

    def ageCategory(age: Int): String = {
      if (age < 30) "Молодой" else "Взрослый"
    }

    val category = ageCategory(age)
    println(s"Возрастная категория: $category")

    println("Числа от 1 до 10:")
    for (i <- 1 to 10) {
      println(i)
    }

    val students = List("Анна", "Петр", "Мария", "Сергей")
    println("Список студентов:")
    for (student <- students) {
      println(student)
    }

    println("\nВведите ваши данные:")
    print("Имя: ")
    val inputName = scala.io.StdIn.readLine()
    print("Возраст: ")
    val inputAge = scala.io.StdIn.readInt()
    print("Студент (true/false): ")
    val inputIsStudent = scala.io.StdIn.readBoolean()

    val inputCategory = ageCategory(inputAge)
    println("\nИнформация о пользователе:")
    println(s"Имя: $inputName")
    println(s"Возраст: $inputAge")
    println(s"Студент: $inputIsStudent")
    println(s"Возрастная категория: $inputCategory")

    val numbers = (1 to 10).toList

    val squares = for (n <- numbers) yield n * n
    println(s"Квадраты чисел: $squares")

    val evens = for {
      n <- numbers
      if n % 2 == 0
    } yield n
    println(s"Четные числа: $evens")
  }
}