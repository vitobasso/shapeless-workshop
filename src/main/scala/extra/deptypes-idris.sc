/*
  Can we translate this "dependent types" example from Idris to Scala?

  isSingleton : Bool -> Type
  isSingleton True = Nat
  isSingleton False = List Nat

  mkSingle : (x : Bool) -> isSingleton x
  mkSingle True = 0
  mkSingle False = []

  http://docs.idris-lang.org/en/latest/tutorial/typesfuns.html
 */

trait IntOrString {
  type Out
}
object IntOrString {
  def apply(bool: Boolean): IntOrString =
    if(true) new IntOrString { type Out = Int }
    else new IntOrString { type Out = String }
}

val result = IntOrString(true)

import extra.DummyGeneration.dummies
dummies[Int]
dummies[String]
dummies[result.Out] //I hoped it would work but it doesn't