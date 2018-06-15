/*
    1.  create the type class Show[A]
        that does:
          def show(a: A): String

    2.  create basic instances for:
          Int
          String
          Boolean
            - shows "yes" or "no"
          Cat(name: String, livesLeft: Int)
            - shows like "Gatarys, 7"

    3.  create an implicit class to enable:
          123.show == "123"

 */

case class Cat(name: String, livesLeft: Int)

// YOUR CODE GOES HERE
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
  implicit val cat = new Show[Cat] {
    override def show(a: Cat) = s"${a.name}, ${a.livesLeft}"
  }
}

implicit class ShowOps[A: Show](a: A) {
  def show(implicit s: Show[A]): String = s.show(a)
}

//goal:
123.show                // "123"
"bla bla".show          // "bla bla"
true.show               // "yes"
Cat("Gatarys", 7).show  // "Gatarys, 7"