import shapeless._

/*
  4. derive EncodeCsv[A]

      String
         Int    ->    HList     ->        A
        (...)                 Generic

   we need:
      EncodeCsv[HList]
         EncodeCsv[HNil]
         EncodeCsv[String]
         EncodeCsv[Int]
         EncodeCsv[...]
      Generic[A]
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

implicit def hnil: EncodeCsv[HNil] = EncodeCsv.instance {
  _: HNil => Nil
}
implicit def hlist[H, T <: HList](implicit
                                  encodeHead: EncodeCsv[H],
                                  encodeTail: EncodeCsv[T]
                                  ): EncodeCsv[H :: T] = EncodeCsv.instance[H :: T] {
  v: (H :: T) => encodeHead.encode(v.head) ++ encodeTail.encode(v.tail)
}
implicit def caseClass[A,H](implicit
                          generic: Generic.Aux[A,H],
                          encodeHlist: EncodeCsv[H]
                         ): EncodeCsv[A] = EncodeCsv.instance {
  v: A =>
    val g = generic.to(v)
    encodeHlist.encode(g)
}

val c = Cat("bla", 7, false)
encode(c)
