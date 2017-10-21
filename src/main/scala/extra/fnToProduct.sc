import shapeless._
import shapeless.ops.function._


type F = (String, Int) => String
type G = String :: Int :: HNil => String
val fp = implicitly[FnToProduct.Aux[F, G]]

val fun: (String, Int) => String = (s, i) => s"result $s, $i"
val fun2: String :: Int :: HNil => String = fp(fun)

fun("a", 1)
fun2("a" :: 1 :: HNil)
