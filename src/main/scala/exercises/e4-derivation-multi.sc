/*
  4. derive Show[A] from smaller parts, for any A

      String
         Int    ->    HList     ->        A
        (...)                 Generic

   we'll need:
      Show[HList]
         Show[HNil]
         Show[String]
         Show[Int]
         Show[...]
      Generic[A]

   goal:
      show(cat) == "Gatarys, 7, yes"
      show(person) == ...
      show(aeroplane) == ...
      ...
 */
trait Show[A] {
  def show(a: A): String
}
object Show {
  def apply[A](implicit e: Show[A]): Show[A] = e
  def instance[A](f: A => String) = new Show[A] {
    override def show(a: A): String = f(a)
  }
}
def show[A](a: A)(implicit e: Show[A]): String = e.show(a)
implicit val int: Show[Int] = Show.instance{ v: Int => v.toString }
implicit val string: Show[String] = Show.instance{ v: String => v }
implicit val bool: Show[Boolean] = Show.instance{ v: Boolean => if(v) "yes" else "no" }


import shapeless._

// YOUR CODE GOES HERE


case class Cat(name: String, livesLeft: Int, female: Boolean)
case class Person(name: String, age: Int)
case class Aeroplane(airline: String, weight: Double)

// goal:
show(Cat("Gatarys", 7, true)) == "Gatarys, 7, yes"
show(Person("Victor", 32)) == "Victor, 32"
show(Aeroplane("Bla", 123.5)) == "Bla, 123.5"