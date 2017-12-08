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


trait LastOfType[L, T] {
  type Out
  def apply(hlist: L): Out
}
object LastOfType {
  type Aux[L, T, O] = LastOfType[L, T] { type Out = O }
}

implicit class CustomHListOps[L <: HList](hlist: L) {
  def lastOfType[T](implicit p: LastOfType[L, T]): p.Out = p.apply(hlist)
}

implicit def hlistLastOfType[L <: HList, T, Filtered <: HList, Rest <: HList, O]
    (implicit
      part: Partition.Aux[L, T, Filtered, Rest],
      last: Last.Aux[Filtered, O]
    ): LastOfType.Aux[L, T, O] =
  new LastOfType[L, T] {
    type Out = O
    def apply(hlist: L): Out = {
      val (filtered, rest) = part(hlist)
      last(filtered)
    }
  }


//it works!
hlist.lastOfType[String]
hlist.lastOfType[Boolean]
