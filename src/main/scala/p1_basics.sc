import shapeless._

//hlist
type Bla = Int :: String :: HNil
val h: Bla = 1 :: "bla" :: HNil
val h2     = 1 :: "bla" :: HNil

type Ble = (Int, String)
val t = (1, "bla")


//generic
case class Person(name: String, age: Int)
val p = Person("Victor", 32)
val gen = Generic[Person]
val r = gen.to(p)

case class IceCream(flavor: String, numCherries: Int)
val gen2 = Generic[IceCream]
gen2.from(r)


//coproduct
type Bli = Int :+: String :+: CNil
val c1: Bli = Inl(1)
val c2: Bli = Inr(Inl("bla"))

type Blo = Either[Int, String]
val e1: Blo = Left(1)
val e2: Blo = Right("bla")


/* ADT = algebraic data type

   product, tuples, records
   "and"
       (String, Int)
       Person(name: String, age: Int)
       String :: Int :: HNil

   coproduct, aka sum type, aka disjoint union, aka variant type
   "or"
       Either[String, Int]
       sealed trait Shape
          case class Circle(width: Double, height: Double) extends Shape
          case class Rectangle(radius: Double) extends Shape
       String :+: Int :+: CNil
 */


//generic with coproducts
sealed trait Shape
case class Rectangle(width: Double, height: Double) extends Shape
case class Circle(radius: Double) extends Shape
val rect: Shape = Rectangle(3.0, 4.0)
val circ: Shape = Circle(1.0)
val r2 = Generic[Shape].to(rect)
val r3: Circle :+: Rectangle :+: CNil = r2
