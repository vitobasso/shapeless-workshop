import shapeless._

//dependencies from previous worksheet
case class Person(name: String, age: Int)
trait Dummy[A] {
  def value: A
}
object Dummy {
  def apply[A](implicit e: Dummy[A]): Dummy[A] = e
  def instance[A](v: A): Dummy[A] = new Dummy[A] {
    override def value: A = v
  }
}
def dummy[A](implicit e: Dummy[A]): A = e.value
implicit val strDummy: Dummy[String] = Dummy.instance("bla")
implicit val intDummy: Dummy[Int] = Dummy.instance(1)

/*
  Derive Dummy[A] from smaller parts, for any A

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

   steps:
      1. Dummy[A]
          needs:
            - Generic[A]
            - Dummy[HList]
      2. Dummy[HList]
          needs:
            - Dummy[Head], which needs:
                - Dummy[HNil]
                - Dummy[String], Dummy[Int], etc
            - Dummy[Tail]  <- that's recursion
 */

























//step 1
def caseClassDummy1[A, Gen <: HList](implicit
                                       hlistDummy: Dummy[Gen],  //we'll define next
                                       gen: Generic.Aux[A, Gen] //shapeless creates for us
                                      ): Dummy[A] = {
  val hlist: Gen = hlistDummy.value
  val a: A = gen.from(hlist)
  Dummy.instance(a)
}

//step 2
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
















//step 2
implicit def hnilDummy: Dummy[HNil] = Dummy.instance(HNil) //base case for the recursion
implicit def hlistDummy1[Head, Tail <: HList](implicit
        head: Dummy[Head],   //we've defined before for String, Int, ...
        tail: Dummy[Tail]    //recursion. last is HNil
       ): Dummy[Head :: Tail] = {
  val h: Head = head.value
  val t: Tail = tail.value
  Dummy.instance(h :: t)
}
Dummy[Int :: String :: HNil].value
  Dummy[Int]
  Dummy[String :: HNil]
    Dummy[String]
    Dummy[HNil]

//step 1 fixed:
//   - order of implicits matters
//   - lazy to workaround "implicit divergence"
implicit def caseClassDummy2[A, Gen <: HList](implicit
        gen: Generic.Aux[A, Gen],  //shapeless creates for us.
        hlistDummy: Lazy[Dummy[Gen]]  //we've defined Dummy[HList] above
       ): Dummy[A] = {
  val hlist: Gen = hlistDummy.value.value
  val a: A = gen.from(hlist)
  Dummy.instance(a)
}


//debug
Dummy[Person].value
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



