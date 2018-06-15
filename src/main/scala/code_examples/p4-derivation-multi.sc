import shapeless._

//dependencies from previous worksheet
case class Person(name: String, age: Int)
trait Dummy[A] {
  def get: A
}
object Dummy {
  def apply[A](implicit e: Dummy[A]): Dummy[A] = e
  def instance[A](v: A): Dummy[A] = new Dummy[A] {
    override def get: A = v
  }
}
def dummy[A](implicit e: Dummy[A]): A = e.get
implicit val strDummy: Dummy[String] = Dummy.instance("bla")
implicit val intDummy: Dummy[Int] = Dummy.instance(1)

/*
  derive Dummy[A] from smaller parts, for any A

  goal: dummy[Person] = Person("bla", 1)
        dummy[Cat] = ...
        dummy[Aeroplane] = ...
        dummy[List[Cat]] = ...
        ...
 */

//Dummy[A] ?
def caseClassDummy[A]: Dummy[A] = ???
/* idea:
    - build upon smaller parts
    - use HList to generalize any type

      String
         Int    ->    HList     ->        A
        (...)                 Generic

   we need:
      Dummy[HList]
         Dummy[HNil]
         Dummy[String]
         Dummy[Int]
         Dummy[...]
      Generic[A]
 */


























def caseClassDummy1[A, Gen <: HList](implicit
                                       e: Dummy[Gen],  //we'll define next
                                       gen: Generic.Aux[A, Gen] //shapeless creates for us
                                      ): Dummy[A] = ???

def hlistDummy[Head, Tail <: HList]: Dummy[Head :: Tail] = ???
//Dummy[Int :: String :: HNil].apply == 1 :: "bla" :: HNil

/* idea: recursive definition
   Dummy[Head :: Tail]
     Dummy[Head]
     Dummy[Tail]

   i.e.
   Dummy[Int :: String :: HNil]
    Dummy[Int]
    Dummy[String :: HNil]
      Dummy[String]
      Dummy[HNil]
 */

















implicit def hnilDummy: Dummy[HNil] = Dummy.instance(HNil) //base case for the recursion
implicit def hlistDummy1[Head, Tail <: HList](implicit
        head: Dummy[Head],   //some Dummy we've defined before: String, Int, ...
        tail: Dummy[Tail]    //recursion. last is HNil
       ): Dummy[Head :: Tail] = {
  val h: Head = head.get
  val t: Tail = tail.get
  Dummy.instance(h :: t)
}
Dummy[Int :: String :: HNil].get
  Dummy[Int]
  Dummy[String :: HNil]
    Dummy[String]
    Dummy[HNil]

implicit def caseClassDummy2[A, Gen <: HList](implicit
        //*order matters*
        gen: Generic.Aux[A, Gen], //shapeless creates for us.
        e: Lazy[Dummy[Gen]]     //we've just defined Dummy[HList]
                                  // *lazy needed so compiler doesn't give up the implicit search on complex cases*
       ): Dummy[A] = {
  val hlist: Gen = e.value.get
  val a: A = gen.from(hlist)
  Dummy.instance(a)
}


//debug
Dummy[Person].get
  Generic[Person]
  Dummy[String :: Int :: HNil]
    Dummy[String]
    Dummy[Int]
    Dummy[HNil]


//it works!
implicit val bool: Dummy[Boolean] = Dummy.instance(true)
case class IceCream(flavor: String, numCherries: Int, hasChocolateSauce: Boolean)
dummy[IceCream]
//*the next one only works with Lazy
case class Gelateria(owner: Person, iceCream: IceCream, isOpen: Boolean)
dummy[Gelateria]



