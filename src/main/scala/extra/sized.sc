//https://stackoverflow.com/questions/28287612/how-to-require-typesafe-constant-size-array-in-scala

import shapeless.{nat, _}
import shapeless.Nat._
import shapeless.Sized
import syntax.sized._

//sized
val a: Option[Sized[String, _1]] = "bla".sized[_1]
val b: Option[Sized[String, _3]] = "bla".sized[_3]

//max size
import ops.nat._
import LT._
def func3[N <: Nat](l: Sized[List[Int], N])(implicit ev: N < _3) = l
func3(List(1,2).sized(2).get)
func3(List(1).sized(1).get)
//func3(List(1,2,3).sized(3).get) // compile error

