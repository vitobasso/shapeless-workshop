import shapeless._

//hlist (heterogeneous list)
type G = Int :: String :: HNil
val h: G = 1 :: "bla" :: HNil

type T = (Int, String)
val t = (1, "bla")

val v1: Int = h.head
import nat._
val v2: String = h(_1)
//val v3: String = h(_2) //doesn't compile


//generic
case class Person(name: String, age: Int)
val p = Person("Victor", 32)
type G2 = String :: Int :: HNil
val genP = Generic[Person]
val pRepr: G2 = genP.to(p)

case class IceCream(flavor: String, numCherries: Int)
val genI = Generic[IceCream]
val i: IceCream = genI.from(pRepr) //intellij is confused, but it compiles


//coproduct
type C = Int :+: String :+: CNil
val c1: C = Inl(1)
val c2: C = Inr(Inl("bla"))

type E = Either[Int, String]
val e1: E = Left(1)
val e2: E = Right("bla")


//generic with coproducts
sealed trait Shape
case class Rectangle(width: Double, height: Double) extends Shape
case class Circle(radius: Double) extends Shape
val genShape = Generic[Shape]
genShape.to(Rectangle(3.0, 4.0))
genShape.to(Circle(1.0))
