import shapeless._
import shapeless.ops.hlist._

//more operations on HList
val hlist = Seq.empty[Int] :: "bla" :: true :: "ble" :: 123 :: HNil
hlist.filter[String]
hlist.reverse
hlist.tupled
hlist.length.toInt


/*
  3. define LastOfType type class by combining:
      - Partition (filter)
      - Last

  goal:
    hlist.lastOfType[String] == ble
    hlist.lastOfType[Boolean] == true

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
    a. define the type class
    b. define the companion, with Aux
    c. define the implicit def (skeleton with ???s)
    d. include the lemmas: Partition & Last
        use their Aux (with ???s as types params)
    e. place the type params properly (so they restrict between the lemmas)
        implement the proof
    f. create the syntax sugar
        show that it works
 */
