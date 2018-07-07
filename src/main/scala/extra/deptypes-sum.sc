/*
Can I implement Sum* without dependent types?

*https://jto.github.io/articles/typelevel_quicksort/
 */

sealed trait Nat
final class _0 extends Nat
final class Succ[P <: Nat]() extends Nat
type _1 = Succ[_0]
type _2 = Succ[_1]
type _3 = Succ[_2]
type _4 = Succ[_3]
type _5 = Succ[_4]

trait Sum[A <: Nat, B <: Nat] { type Out <: Nat }

object Sum {
  def apply[A <: Nat, B <: Nat](implicit sum: Sum[A, B]): Aux[A, B, sum.Out] = sum

  type Aux[A <: Nat, B <: Nat, C <: Nat] = Sum[A, B] { type Out = C }

  implicit def sum1[B <: Nat]: Aux[_0, B, B] = new Sum[_0, B] { type Out = B }

  implicit def sum2[A <: Nat, B <: Nat]
  (implicit sum : Sum[A, Succ[B]]): Aux[Succ[A], B, sum.Out] = new Sum[Succ[A], B] { type Out = sum.Out }
}

val sum = implicitly[Sum[_1, _1]]
val bla: sum.Out = new Succ[Succ[_0]]