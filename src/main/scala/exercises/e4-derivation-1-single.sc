/*
    Derive `Show` for any wrapper
      - "wrapper" here means a case class with only one field

    steps:
      1. draft signature for the generator
        - implicit def ... : Show[A]
      2. add implicit params
        - a Generic, to convert A to an HList of smaller parts
        - a Show of the smaller part (the wrapped type)
      3. impl the function body
        - use the implicit params, wire them together

      try to run now, then:

      5. suffer
        - get "implicit not found" or "implicit divergence"
      6. debug
        - check if the implicit params themselves can be found
          - implicitly[Generic.Aux...]
          - implicitly[Show[Double]] ...
      7. play with the implicit params in the signature
        - change the order
        - wrap one with Lazy
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

//goal:               should return:
Weight(20.1).show     // "wrapped 20.1"
Distance(456).show    // "wrapped 456"
Latitude(-3.123).show // "wrapped -3.123"
