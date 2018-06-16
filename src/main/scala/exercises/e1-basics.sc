
/*
  Convert Cat -> HList -> Book
 */
case class Cat(name: String, livesLeft: Int, isFemale: Boolean)
case class Book(title: String, pages: Int, isHardcover: Boolean)

val cat = Cat("Your cat's name", 7, true)

import shapeless._

//YOUR CODE GOES HERE
val hlist = ???
val book = ???
