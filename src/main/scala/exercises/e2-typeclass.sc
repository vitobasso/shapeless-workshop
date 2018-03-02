/*
    1.  create the type class Show[A]
        that does:
          def show(a: A): String

    2.  create basic instances for:
          Int
          String
          Boolean
            - shows "yes" or "no"
          Person(name: String, age: Int)
            - shows like "John Doe, 32"

    3.  create syntax sugar so you can do:
          123.show == "123
          Person("John Doe", 32).show == "John Doe, 32"

 */

case class Person(name: String, age: Int)

trait Show[A] {
  def show(a: A): String
}
object Show {
  implicit val int = new Show[Int] {
    override def show(a: Int) = a.toString
  }
  implicit val string = new Show[String] {
    override def show(a: String) = a
  }
  implicit val boolean = new Show[Boolean] {
    override def show(a: Boolean) = if(a) "yes" else "no"
  }
  implicit val person = new Show[Person] {
    override def show(a: Person) = s"${a.name}, ${a.age}"
  }
}

implicit class ShowOps[A: Show](a: A) {
  def show(implicit s: Show[A]): String = s.show(a)
}


123.show
"bla bla".show
true.show
Person("John Doe", 32).show