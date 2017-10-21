//https://stackoverflow.com/questions/28287612/how-to-require-typesafe-constant-size-array-in-scala

import shapeless.nat._
import shapeless.syntax.sized._
import shapeless.{nat, _}

object If{
  def apply[T, F, G](t: T, f: F, g: G)
                    (implicit ea: EApply[T, F, G]): ea.Out = ea(t, f, g)
}

trait EApply[T, F, G]{
  type Out
  def apply(t: T, f: F, g: G): Out
}

object EApply extends LowPriorityEApply{
  def apply[T, F, G](implicit ea: EApply[T, F, G]) = ea

  implicit def fapply[T, R, G] = new EApply[T, T => R, G] {
    type Out = R
    def apply(t: T, f: T => R, g: G) = f(t)
  }
}
trait LowPriorityEApply{
  implicit def gapply[T, R, F] = new EApply[T, F, T => R] {
    type Out = R
    def apply(t: T, f: F, g: T => R) = g(t)
  }
}

val res1: Int = If(1, {x: Int => 42}, {x: Double => "no"})
//res1 == 42)

val res2: String = If(2.0, {x: Int => 42}, {x: Double => "no"})
//res2 == "no")