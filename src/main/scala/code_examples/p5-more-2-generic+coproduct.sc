import shapeless.Generic
import shapeless.{:+:, CNil}

//generic with coproducts
sealed trait Shape
case class Rectangle(width: Double, height: Double) extends Shape
case class Circle(radius: Double) extends Shape
val genShape = Generic[Shape]
type G = Circle :+: Rectangle :+: CNil
val r: G = genShape.to(Rectangle(3.0, 4.0))
val c: G = genShape.to(Circle(1.0))
