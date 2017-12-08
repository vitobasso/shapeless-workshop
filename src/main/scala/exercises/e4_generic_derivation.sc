import shapeless._

/*
  4. derive EncodeCsv[A] from smaller parts, for any A

      String
         Int    ->    HList     ->        A
        (...)                 Generic

   we'll need:
      EncodeCsv[HList]
         EncodeCsv[HNil]
         EncodeCsv[String]
         EncodeCsv[Int]
         EncodeCsv[...]
      Generic[A]

   goal:
      encode(cat) == List(Gatarys, 7, yes)
      encode(person) == ...
      encode(aeroplane) == ...
      ...
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
