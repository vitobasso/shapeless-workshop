//https://stackoverflow.com/questions/28287612/how-to-require-typesafe-constant-size-array-in-scala

import shapeless.Nat._
import shapeless.{Sized, _}
import shapeless.syntax.sized._

//sized
val a: Option[String Sized _1] = "bla".sized[_1] //None
val b: Option[String Sized _3] = "bla".sized[_3] //Some

//limit check
import shapeless.ops.nat.LT.<
def checkLT3[N <: Nat](l: Sized[List[Int], N])(implicit ev: N < _3) = l
checkLT3(List(1).sized(1).get)
checkLT3(List(1,2).sized(2).get)
//checkLT3(List(1,2,3).sized(3).get) // compile error

