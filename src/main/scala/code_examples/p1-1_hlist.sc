
//hlist (heterogeneous list)
import shapeless.{::, HNil}

type G = Int :: String :: HNil
val h: G = 1 :: "bla" :: HNil

type T = (Int, String)
val t = (1, "bla")

val v1: Int = h.head
val v2: String :: HNil = h.tail

import shapeless.nat._
val v3: String = h(_1)
//val v4: String = h(_2) //doesn't compile
