import shapeless._

//dependencies from previous worksheet
case class Person(name: String, age: Int)
trait Example[A] {
  def get: A
}
object Example {
  def apply[A](implicit e: Example[A]): Example[A] = e
  def instance[A](v: A): Example[A] = new Example[A] {
    override def get: A = v
  }
}
def example[A](implicit e: Example[A]): A = e.get
implicit val strExample: Example[String] = Example.instance("bla")
implicit val intExample: Example[Int] = Example.instance(1)


//Example[A] ?
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
  val h: Head = head.get
  val t: Tail = tail.get
  Example.instance(h :: t)
}
Example[Int :: String :: HNil].get
  Example[Int]
  Example[String :: HNil]
    Example[String]
    Example[HNil]

implicit def caseClassExample2[A, Gen <: HList](implicit
        //*order matters*
        gen: Generic.Aux[A, Gen], //shapeless creates for us.
        e: Lazy[Example[Gen]]     //we've just defined Example[HList]
                                  // *lazy needed so compiler doesn't give up the implicit search on complex cases*
       ): Example[A] = {
  val hlist: Gen = e.value.get
  val a: A = gen.from(hlist)
  Example.instance(a)
}


//debug
Example[Person].get
  Generic[Person]
  Example[String :: Int :: HNil]
    Example[String]
    Example[Int]
    Example[HNil]


//it works!
implicit val bool: Example[Boolean] = Example.instance(true)
case class IceCream(flavor: String, numCherries: Int, hasChocolateSauce: Boolean)
example[IceCream]
case class Gelateria(owner: Person, iceCream: IceCream, isOpen: Boolean)
example[Gelateria]



