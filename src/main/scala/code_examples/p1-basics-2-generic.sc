import shapeless.{::, HNil}

//generic
import shapeless.Generic

case class Cat(name: String, age: Int)
val c = Cat("Gatarys", 2)

type G = String :: Int :: HNil
val genC = Generic[Cat]
val g = genC.to(c)

case class IceCream(flavor: String, numCherries: Int)
val genI = Generic[IceCream]
val i: IceCream = genI.from(g)

/*
  IntelliJ gets confused with macros.
  https://intellij-support.jetbrains.com/hc/en-us/community/posts/205997409-How-to-make-IntelliJ-recognise-code-created-by-macros-
 */