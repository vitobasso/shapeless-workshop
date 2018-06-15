
//type class
trait Dummy[A] {
  def value: A
}

object Dummy {

  //type class instances

  implicit val int = new Dummy[Int] {
    override def value = 123
  }

  implicit val string = new Dummy[String] {
    override def value = "bla"
  }

  implicit val boolean = new Dummy[Boolean] {
    override def value = true
  }

}

//syntax sugar
def dummy[A](implicit d: Dummy[A]): A = d.value


//usage
dummy[Int]
dummy[String]
dummy[Boolean]



//adding new types separately
case class Cat(name: String, livesLeft: Int)
implicit val dummyCat = new Dummy[Cat] {
  override def value = Cat(dummy[String], dummy[Int])
}
dummy[Cat]