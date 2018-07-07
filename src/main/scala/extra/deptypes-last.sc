//impl of the Last type class copied from the shapeless src

import shapeless._

trait Last[L <: HList] {
  type Out
  def apply(t: L): Out
}

object Last {
  def apply[L <: HList](implicit last: Last[L]): Aux[L, last.Out] = last

  type Aux[L <: HList, Out0] = Last[L] { type Out = Out0 }

  implicit def hsingleLast[H]: Aux[H :: HNil, H] =
    new Last[H :: HNil] {
      type Out = H
      def apply(l : H :: HNil): Out = l.head
    }

  implicit def hlistLast[H, T <: HList]
  (implicit lt : Last[T]): Aux[H :: T, lt.Out] =
    new Last[H :: T] {
      type Out = lt.Out
      def apply(l : H :: T): Out = lt(l.tail)
    }
}

Last[String :: Int :: HNil].apply("bla" :: 123 :: HNil)

implicit class Ops[H <: HList](hlist: H) {
  def last(implicit last: Last[H]): last.Out = last(hlist)
}

("bla" :: 123 :: HNil).last