import shapeless.Generic

def generic[A](a: A)(implicit gen: Generic[A]): gen.Repr = gen.to(a)

case class Cat(name: String, livesLeft: Int)

generic(Cat("jinkx", 7))