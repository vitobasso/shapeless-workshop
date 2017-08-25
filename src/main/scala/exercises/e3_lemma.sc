import shapeless._

/*
  3. derive EncodeCsv[Cat] from basic instances + generic (lemma pattern)
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


//answer
import nat._
type G = String :: Int :: Boolean :: HNil
implicit def cat(implicit
        s: EncodeCsv[String],
        i: EncodeCsv[Int],
        b: EncodeCsv[Boolean],
        g: Generic.Aux[Cat, G]
       ): EncodeCsv[Cat] =
  EncodeCsv.instance {
    cat: Cat =>
      val gen: G = g.to(cat)
      val p1: String = gen(_0)
      val p2: Int = gen(_1)
      val p3: Boolean = gen(_2)
      s.encode(p1) ++ i.encode(p2) ++ b.encode(p3)
  }

val c = Cat("Gatarys", 7, true)
val csv = encode(c)

//debug
EncodeCsv[String]
EncodeCsv[Int]
EncodeCsv[Boolean]
Generic[Cat]
implicitly[Generic.Aux[Cat, G]]
