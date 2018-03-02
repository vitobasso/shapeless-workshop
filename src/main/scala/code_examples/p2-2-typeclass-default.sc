
//type class
trait Default[A] {
  def value: A
}

object Default {

  //type class instances

  implicit val int = new Default[Int] {
    override def value = 123
  }

  implicit val string = new Default[String] {
    override def value = "bla"
  }

  implicit val boolean = new Default[Boolean] {
    override def value = true
  }

}

//syntax sugar
def default[A](implicit d: Default[A]): A = d.value


//usage
default[Int]
default[String]
default[Boolean]
