import shapeless._


//1. convert Cat -> HList -> Book
case class Cat(name: String, livesLeft: Int, female: Boolean)
case class Book(title: String, pages: Int, hardcover: Boolean)

//answer
val c: Cat = Cat("Gatarys", 7, true)
type G = String :: Int :: Boolean :: HNil
val cc: G = Generic[Cat].to(c)
val gen2: Book = Generic[Book].from(cc)