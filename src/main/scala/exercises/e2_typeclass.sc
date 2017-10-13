/*
  2.1 create the type class EncodeCsv[A]
      that does:
        def encode(a: A): List[String]
  2.2 create the sugar methods:
      summoner (apply)
      constructor (instance/pure)
      "syntax" (encode)
  2.3 create basic instances for:
      Int
      String
      Boolean (make it return "yes" or "no")
  2.4 prove that it works:
      encode(true) should return List("yes")
 */

//answer
trait EncodeCsv[A] {
  def encode(a: A): List[String]
}

object EncodeCsv {
  def apply[A](implicit e: EncodeCsv[A]): EncodeCsv[A] = e
  def instance[A](f: A => List[String]) = new EncodeCsv[A] {
    override def encode(a: A): List[String] = f(a)
  }
}
def encode[A](a: A)(implicit e: EncodeCsv[A]): List[String] = e.encode(a)

implicit val int: EncodeCsv[Int] = EncodeCsv.instance {
  v: Int => List(v.toString)
}

implicit val string: EncodeCsv[String] = EncodeCsv.instance {
  v: String => List(v)
}

implicit val bool: EncodeCsv[Boolean] = EncodeCsv.instance {
  v: Boolean => List(if(v) "yes" else "no")
}

encode(123)
encode("bla")
encode(true)
