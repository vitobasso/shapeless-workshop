
//coproduct
import shapeless.{:+:, CNil, Inl, Inr}

type C = Int :+: String :+: CNil
val c1: C = Inl(1)
val c2: C = Inr(Inl("bla"))

type E = Either[Int, String]
val e1: E = Left(1)
val e2: E = Right("bla")

val h: Option[Int] = c2.head
val t: Option[String :+: CNil] = c2.tail
