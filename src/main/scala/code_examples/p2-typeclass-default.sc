
//type class
trait Default[A] {
  def apply: A
}

object Default {

  implicit val defInt: Default[Int] = new Default[Int] {
    override def apply = 123
  }

  implicit val defString: Default[String] = new Default[String] {
    override def apply = "bla"
  }

  implicit val defBoolean: Default[Boolean] = new Default[Boolean] {
    override def apply = true
  }

}

//syntax sugar
def default[A](implicit d: Default[A]): A = d.apply

//usage
default[Int]
default[String]
default[Boolean]
