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
val r1: Int = last(hlist)

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
val r2: Matching = partition.filter(hlist)

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
trait LastOfType[A, T] {
  def apply(list: A): T
}
implicit def genLOT[A <: HList, Last, Matching <: HList, NotMatching <: HList](implicit
                                                    part: Partition.Aux[A, Last, Matching, NotMatching],
                                                    last: Last.Aux[Matching, Last]
                                                    ): LastOfType[A, Last] =
  new LastOfType[A, Last] {
    override def apply(list: A): Last = last(part.filter(list))
  }
implicit class HListOps2[A <: HList](list: A) {
  def lastOfType[O](implicit lot: LastOfType[A, O]): O = lot(list)
}


// goal:
hlist.lastOfType[String]
hlist.lastOfType[Boolean]
