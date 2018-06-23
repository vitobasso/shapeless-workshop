import shapeless.{::, HNil}

//generic
import shapeless.Generic

case class Cat(name: String, age: Int)
val c = Cat("Gatarys", 2)

type H = String :: Int :: HNil
val genC = Generic[Cat]
val g = genC.to(c)

case class IceCream(flavor: String, numCherries: Int)
val genI = Generic[IceCream]
val i: IceCream = genI.from(g)
