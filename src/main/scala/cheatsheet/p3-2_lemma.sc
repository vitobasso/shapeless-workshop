import shapeless._
import shapeless.ops.hlist._

//operations on HList
val hlist = Seq.empty[Int] :: "bla" :: true :: 123 :: HNil
hlist.head
hlist.tail
hlist.last
hlist.init


/*
  3. define Penultimate type class by combining:
      - Init
      - Last

  goal: hlist.penultimate == true
 */


trait Penultimate[L] {
  type Out
  def apply(hlist: L): Out
}
object Penultimate {
  type Aux[L, O] = Penultimate[L] { type Out = O }
}

implicit class CustomHListOps[L <: HList](hlist: L) {
  def penultimate[A](implicit p: Penultimate.Aux[L, A]): A = p.apply(hlist)
}

implicit def hlistPenultimate[L <: HList, M <: HList, O](implicit
                                                         init: Init.Aux[L, M],
                                                         last: Last.Aux[M, O]
                                                        ): Penultimate.Aux[L, O] =
  new Penultimate[L] {
    type Out = O
    def apply(hlist: L): Out =
      last(init(hlist))
  }


//it works!
hlist.penultimate
