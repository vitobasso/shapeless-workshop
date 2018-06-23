import shapeless._
import shapeless.ops.adjoin.Adjoin

val a: String :: HNil = "bla" :: HNil
val b: Int :: HNil = 123 :: HNil
val nested: (String :: HNil) :: Int :: HNil = a :: b

val adjoin = implicitly[Adjoin[(String :: HNil) :: Int :: HNil]]
val flattened = adjoin(nested) //String :: Int :: HNil