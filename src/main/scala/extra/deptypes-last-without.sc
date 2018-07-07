//impl of the Last type class without "dependent types" works just as good
//except that now we always need to specify the Out type param (as we'd do when using Aux anyways)

import shapeless._

trait Last[L <: HList, Out] {
  def apply(t: L): Out
}

object Last {
  def apply[L <: HList, Out](implicit last: Last[L, Out]): Aux[L, Out] = last

  type Aux[L <: HList, Out] = Last[L, Out]

  implicit def hsingleLast[H]: Aux[H :: HNil, H] =
    new Last[H :: HNil, H] {
      def apply(l : H :: HNil): H = l.head
    }

  implicit def hlistLast[H, T <: HList, Out]
  (implicit lt : Last[T, Out]): Aux[H :: T, Out] =
    new Last[H :: T, Out] {
      def apply(l : H :: T): Out = lt(l.tail)
    }
}

Last[String :: Int :: HNil, Int].apply("bla" :: 123 :: HNil)

implicit class Ops[H <: HList](hlist: H) {
  def last[Out](implicit last: Last[H, Out]): Out = last(hlist)
}

("bla" :: 123 :: HNil).last