import shapeless._

//dependencies from previous worksheet
trait Example[A] {
  def apply: A
}
object Example {
  def apply[A](implicit e: Example[A]): Example[A] = e
  def instance[A](v: A): Example[A] = new Example[A] {
    override def apply: A = v
  }
}
implicit val strExample: Example[String] = Example.instance("bla")
implicit val intExample: Example[Int] = Example.instance(1)


//Example[A] ? (type class derivation)
def caseClassExample[A]: Example[A] = ???
/* idea: given the parts, build the whole
      String
         Int    ->    HList     ->        A
        (...)                 Generic

   we need:
      Example[HList]
         Example[String]
         Example[Int]
         Example[...]
      Generic[A]
 */
def caseClassExample1[A, Gen <: HList](implicit
                                       e: Example[Gen],         //we'll define next
                                       gen: Generic.Aux[A, Gen] //shapeless creates for us
                                      ): Example[A] = ???

def hlistExample[Head, Tail <: HList]: Example[Head :: Tail] = ???
//Example[Int :: String :: HNil].apply == 1 :: "bla" :: HNil

/* idea: recursive definition
   Example[Head :: Tail]
     Example[Head]
     Example[Tail]

   i.e.
   Example[Int :: String :: HNil].apply
    Example[Int]
    Example[String :: HNil]
      Example[String]
      Example[HNil]
 */
implicit def hnilExample: Example[HNil] = Example.instance(HNil) //base case for the recursion
implicit def hlistExample1[Head, Tail <: HList](implicit
        head: Example[Head],   //some Example we've defined before: String, Int, ...
        tail: Example[Tail]    //recursion. last is HNil
       ): Example[Head :: Tail] = {
  val h: Head = head.apply
  val t: Tail = tail.apply
  Example.instance(h :: t)
}
Example[Int :: String :: HNil].apply
  Example[Int]
  Example[String :: HNil]
    Example[String]
    Example[HNil]

implicit def caseClassExample2[A, Gen <: HList](implicit
        //*order matters*
        gen: Generic.Aux[A, Gen], //shapeless creates for us.
        e: Lazy[Example[Gen]]     //we've just defined Example[HList] *lazy needed*
       ): Example[A] = {
  val hlist: Gen = e.value.apply
  val a: A = gen.from(hlist)
  Example.instance(a)
}

case class Person(name: String, age: Int)
Example[Person].apply
  Generic[Person]
  Example[String :: Int :: HNil].apply

implicit val bool: Example[Boolean] = Example.instance(true)
case class IceCream(flavor: String, numCherries: Int, hasChocolateSauce: Boolean)
Example[IceCream].apply
case class Castle(hasTowers: Boolean, king: Person, typicalIceCream: IceCream)
Example[Castle].apply



