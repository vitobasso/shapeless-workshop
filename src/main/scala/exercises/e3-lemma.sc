/*
  Define LastOfType type class by combining:
      - Partition (filter)
      - Last
 */

import shapeless._
import shapeless.ops.hlist._

val hlist = Seq.empty[Int] :: "bla" :: true :: "ble" :: 123 :: HNil
type InputHList = Seq[Int] :: String :: Boolean :: String :: Int :: HNil

/*
  Last.Aux[L, O]
      L: input hlist
      O: type of the last element
 */
val last = implicitly[Last.Aux[InputHList, Int]]
val res1: Int = last(hlist)

/*
  Partition.Aux[L, U, Prefix, Suffix]
      L: input hlist
      U: type to filter on
      Prefix: output hlist of elements matching the filter
      Suffix: output hlist of elements not matching
 */
// filtering on String
type Matching = String :: String :: HNil
type NotMatching = Seq[Int] :: Boolean :: Int :: HNil
val partition = implicitly[Partition.Aux[InputHList, String, Matching, NotMatching]]
val res2: Matching = partition.filter(hlist)

/*
  steps:
    1. define the type class
    2. define the companion, with Aux
    3. define the implicit def (signature and ???s)
    4. include the lemmas: Partition & Last
        use their Aux (with ???s as types params for now)
    5. place the type params properly (so they restrict between the lemmas)
    6. implement the function body (a.k.a. proof)
    7. create syntax sugar
        show that it works
 */

// YOUR CODE GOES HERE

// goal:
hlist.lastOfType[String] == "ble"
hlist.lastOfType[Boolean] == true
