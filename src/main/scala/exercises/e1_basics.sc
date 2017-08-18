import shapeless.Generic.Aux
import shapeless._


//1. convert Cat -> HList -> Book
case class Cat(name: String, livesLeft: Int, female: Boolean)
case class Book(title: String, pages: Int, hardcover: Boolean)
val c = Cat("Gatarys", 7, true)

val cc = Generic[Cat].to(c)
val gen2: Book = Generic[Book].from(cc)