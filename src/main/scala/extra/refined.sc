//https://stackoverflow.com/questions/28287612/how-to-require-typesafe-constant-size-array-in-scala

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._
import eu.timepit.refined.boolean._
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import shapeless._
import shapeless.tag.@@
import shapeless.Nat._
import eu.timepit.refined.auto._
import eu.timepit.refined.string.MatchesRegex

val s1: String Refined NonEmpty                   = "Hello"
//val s2: String Refined NonEmpty                   = "" // compile error
val s4: String @@ NonEmpty                        = "Hello"
val s3: Either[String, Refined[String, NonEmpty]] = refineV[NonEmpty]("")
val s5: Either[String, String @@ NonEmpty]        = refineT[NonEmpty]("Hello")

val n1: Int Refined Positive                      = 5
//val n2: Int Refined Positive                      = -5  //compile error
type ZeroToOne = Not[Less[W.`0.0`.T]] And Not[Greater[W.`1.0`.T]]
val n3: Double Refined ZeroToOne                  = 0.5
//val n2: Double Refined ZeroToOne                = 1.2 // compile error

type CustomChar = AnyOf[Digit :: Letter :: Whitespace :: HNil]
val ss1: Char Refined CustomChar                  = 'F'
//val ss2: Char Refined CustomChar                  = '?' //compile error

val ss2: String Refined MatchesRegex[W.`"[0-9]+"`.T] = "123"

val lt10: Int Refined Less[_10]                   = 5
val lt11: Int Refined Less[_11]                   = lt10
//val lt9: Int Refined Less[_9]                     = lt10 //compile error
