
//hlist: heterogeneous list
import shapeless._

type H = Int :: String :: HNil
val h: H = 1 :: "bla" :: HNil

type T = (Int, String)
val t: T = (1, "bla")

val v1: Int = h.head
val v2: String :: HNil = h.tail

import shapeless.nat._
val v3: Int = h(_0)
val v4: String = h(_1)
//val v5: String = h(_2) //doesn't compile

h.last
h.init
h.length.toInt
h.filter[String]
h.reverse
h.tupled
h.replace("ble")
type H2 = Double :: String :: HNil
h.intersect[H2]
h.diff[H2]
type G2 = String :: Int :: HNil
h.align[G2]
