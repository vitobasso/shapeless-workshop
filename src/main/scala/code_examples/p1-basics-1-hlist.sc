
//hlist (heterogeneous list)
import shapeless._

type G = Int :: String :: HNil
val h: G = 1 :: "bla" :: HNil

type T = (Int, String)
val t = (1, "bla")

val v1: Int = h.head
val v2: String :: HNil = h.tail

import shapeless.nat._
val v3: String = h(_1)
//val v4: String = h(_2) //doesn't compile

h.last
h.init
h.length.toInt
h.filter[String]
h.reverse
h.tupled
h.replace("ble")
type G1 = Double :: String :: HNil
h.intersect[G1]
h.diff[G1]
type G2 = String :: Int :: HNil
h.align[G2]
