import shapeless._
import shapeless.ops.hlist._

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
 */
