/*
    Derive `Show` for any wrapper
      - "wrapper" here means a case class with only one field
 */

trait Show[A] {
  def show(a: A): String
}
object Show {
  implicit val double = new Show[Double] {
    override def show(a: Double) = a.toString
  }
}
implicit class ShowOps[A: Show](a: A) {
  def show(implicit s: Show[A]): String = s.show(a)
}

//wrappers
case class Weight(value: Double)
case class Distance(value: Double)
case class Latitude(value: Double)

import shapeless._

//YOUR CODE GOES HERE
implicit def wrapper[A, B](implicit
                           gen: Lazy[Generic.Aux[A, B :: HNil]],
                           showWrapped: Show[B]
                          ) =
  new Show[A] {
    override def show(a: A) = {
      val wrapped = gen.value.to(a).head
      "wrapped " + showWrapped.show(wrapped)
    }
  }

//goal:
Weight(20.1).show     // "wrapped 20.1"
Distance(456).show    // "wrapped 456"
Latitude(-3.123).show // "wrapped -3.123"
