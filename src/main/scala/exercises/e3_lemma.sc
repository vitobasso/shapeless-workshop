import shapeless._

/*
  3. derive EncodeCsv[Cat] smaller parts:
    - EncodeCsv[String]
    - EncodeCsv[Int]
    - EncodeCsv[Boolean]
    - Generic[Person]

      String
         Int    ->    HList     ->        Cat
     Boolean                  Generic

    goal:
      val cat = Cat("Gatarys", 7, true)
      encode(cat) == List(Gatarys, 7, yes)
 */
case class Cat(name: String, livesLeft: Int, female: Boolean)
trait EncodeCsv[A] {
  def encode(a: A): List[String]
}
object EncodeCsv {
  def apply[A](implicit e: EncodeCsv[A]): EncodeCsv[A] = e
  def instance[A](f: A => List[String]) = new EncodeCsv[A] {
    override def encode(a: A): List[String] = f(a)
  }
}
implicit val int: EncodeCsv[Int] = EncodeCsv.instance{
  v: Int => List(v.toString)
}
implicit val string: EncodeCsv[String] = EncodeCsv.instance{
  v: String => List(v)
}
implicit val bool: EncodeCsv[Boolean] = EncodeCsv.instance{
  v: Boolean => List(if(v) "yes" else "no")
}
def encode[A](a: A)(implicit e: EncodeCsv[A]): List[String] = e.encode(a)
