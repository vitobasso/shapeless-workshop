//https://stackoverflow.com/questions/28287612/how-to-require-typesafe-constant-size-array-in-scala

import shapeless.{nat, _}
import nat._
import shapeless.ops.nat.ToInt
import syntax.sized._

val a: Option[Sized[String, _1]] = "bla".sized(_1)
val b: Option[Sized[String, _3]] = "bla".sized(_3)

type String3 = Sized[String, _3]
def doSomething(value: String3) = ???

////specific size
//def func1(l: Sized[List[Int], _3]) = l
//List(1,2,3,4,5,6).grouped(3).map(_.sized(3).get).map(func1)
//List(1,2,3,4,5,6).grouped(3).map(_.sized(3).get).map(func1)
//
////max size
//import ops.nat._
//import LT._
//def func3[N <: Nat](l: Sized[List[Int], N])(implicit ev: N < _3) = l
//func3(List(1,2).sized(2).get)
//func3(List(1).sized(1).get)
//func3(List(1,2,3).sized(3).get)
//

