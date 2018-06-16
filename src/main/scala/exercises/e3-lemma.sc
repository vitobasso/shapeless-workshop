/*
  Define LastOfType type class by combining:
      - Partition (filter)
      - Last

  type classes used:
    Last.Aux[L, O]
      L: input hlist
      O: output (type of the last element)
    Partition.Aux[L, T, Filtered, Rest]
      L: input hlist
      T: type to filter on
      Filtered: output hlist of elements matching the filter
      Rest: output hlist of elements not matching

  steps:
    1. define the type class
    2. define the companion, with Aux
    3. define the implicit def (signature and ???s)
    4. include the lemmas: Partition & Last
        use their Aux (with ???s as types params for now)
    5. place the type params properly (so they restrict between the lemmas)
    6. implement the function body (a.k.a. proof)
    6. create the syntax sugar
        show that it works
 */

import shapeless._
import shapeless.ops.hlist._

val hlist = Seq.empty[Int] :: "bla" :: true :: "ble" :: 123 :: HNil

// YOUR CODE GOES HERE

// goal:
hlist.lastOfType[String] == "ble"
hlist.lastOfType[Boolean] == true
