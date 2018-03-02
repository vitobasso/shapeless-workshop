



//standard scala

implicitly[scala.Numeric[Int]]
List(1, 2, 3).sum
//List("bla", "ble").sum //doesn't compile

implicitly[scala.Ordering[Int]]
List(1, 2, 3).max




//cats

import cats.instances.int._
import cats.syntax.show._
import cats.syntax.order._
import cats.syntax.semigroup._

cats.Show[Int]
123.show

cats.Eq[Int]
1 === 2

cats.Order[Int]
1 comparison 2

cats.Semigroup[Int]
1 |+| 2




//shapeless

import shapeless._
import shapeless.ops.hlist._

Last[Int :: String ::HNil]
(1 :: "bla" :: HNil).last

type H = String :: Int :: String :: Int :: HNil
Filter[H, Int]
(1 :: "bla" :: 2 :: "ble" :: HNil).filter[Int]