/*
  Derive Show[A] from smaller parts, for any A

      String
         Int    ->    HList     ->        A
        (...)                 Generic

   steps:
      1. Show[A]
          needs:
            - Generic[A]
            - Show[HList]
      2. Show[HList]
          needs:
            - Show[Head], which needs:
                - Show[HNil]
                - Show[String], Show[Int], etc
            - Show[Tail]  <- that's recursion

 */

//type class
trait Show[A] {
  def show(a: A): String
}
object Show {
  def apply[A](implicit e: Show[A]): Show[A] = e
  def instance[A](f: A => String) = new Show[A] {
    override def show(a: A): String = f(a)
  }
}
implicit class ShowOps[A: Show](a: A) {
  def show(implicit s: Show[A]): String = s.show(a)
}

//base instances
implicit val int: Show[Int] = Show.instance{ v: Int => v.toString }
implicit val string: Show[String] = Show.instance{ v: String => v }
implicit val bool: Show[Boolean] = Show.instance{ v: Boolean => if(v) "yes" else "no" }

case class Cat(name: String, livesLeft: Int, female: Boolean)
case class Person(name: String, age: Int)
case class Aeroplane(airline: String, weight: Double)

import shapeless._

// YOUR CODE GOES HERE

// goal:                          should return:
Cat("Gatarys", 7, true).show      //"Gatarys, 7, yes"
Person("Victor", 32).show         //"Victor, 32"
Aeroplane("Bla", 123.5).show      //"Bla, 123.5"