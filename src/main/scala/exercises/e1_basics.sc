import shapeless._

/*
  1. convert Cat -> HList -> Book
 */
case class Cat(name: String, livesLeft: Int, female: Boolean)
case class Book(title: String, pages: Int, hardcover: Boolean)
