//https://stackoverflow.com/questions/51131067/when-are-dependent-types-needed-in-shapeless/51144080?noredirect=1#comment89322622_51144080

sealed trait Nat
case object Zero extends Nat
type Zero = Zero.type
case class Succ[N <: Nat](n: N) extends Nat

type One = Succ[Zero]
type Two = Succ[One]
type Three = Succ[Two]
type Four = Succ[Three]
type Five = Succ[Four]

val one: One = Succ(Zero)
val two: Two = Succ(one)
val three: Three = Succ(two)
val four: Four = Succ(three)
val five: Five = Succ(four)

//trait Add[N <: Nat, M <: Nat] {
//  type Out <: Nat
//  def apply(n: N, m: M): Out
//}
//
//object Add {
//  type Aux[N <: Nat, M <: Nat, Out0 <: Nat] = Add[N, M] { type Out = Out0 }
//  def instance[N <: Nat, M <: Nat, Out0 <: Nat](f: (N, M) => Out0): Aux[N, M, Out0] = new Add[N, M] {
//    override type Out = Out0
//    override def apply(n: N, m: M): Out = f(n, m)
//  }
//
//  implicit def zeroAdd[M <: Nat]: Aux[Zero, M, M] = instance((_, m) => m)
//  implicit def succAdd[N <: Nat, M <: Nat, N_addM <: Nat](implicit add: Aux[N, M, N_addM]): Aux[Succ[N], M, Succ[N_addM]] =
//    instance((succN, m) => Succ(add(succN.n, m)))
//}
//
//implicitly[Add[One, Two]].apply(one, two)

trait Add[N <: Nat, M <: Nat, Out <: Nat] {
  def apply(n: N, m: M): Out
}

object Add {
  implicit def zeroAdd[M <: Nat]: Add[Zero, M, M] = (_, m) => m
  implicit def succAdd[N <: Nat, M <: Nat, N_addM <: Nat](implicit add: Add[N, M, N_addM]): Add[Succ[N], M, Succ[N_addM]] =
    (succN, m) => Succ(add(succN.n, m))
}

implicitly[Add[One, Two, Three]].apply(one, two)


implicit class AddOps[A <: Nat](nat: A) {
  def +[B <: Nat, C <: Nat](other: B)(implicit add: Add[A, B, C]): C = add(nat, other)
}

one + three