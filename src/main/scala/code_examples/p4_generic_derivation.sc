import shapeless._

//dependencies from previous worksheet
case class Person(name: String, age: Int)
trait SampleInstance[A] {
  def get: A
}
object SampleInstance {
  def apply[A](implicit e: SampleInstance[A]): SampleInstance[A] = e
  def instance[A](v: A): SampleInstance[A] = new SampleInstance[A] {
    override def get: A = v
  }
}
def sample[A](implicit e: SampleInstance[A]): A = e.get
implicit val strSampleInstance: SampleInstance[String] = SampleInstance.instance("bla")
implicit val intSampleInstance: SampleInstance[Int] = SampleInstance.instance(1)

/*
  derive SampleInstance[A] from smaller parts, for any A

  goal: sample[Person] = Person("bla", 1)
        sample[Cat] = ...
        sample[Aeroplane] = ...
        sample[List[Cat]] = ...
        ...
 */

//SampleInstance[A] ?
def caseClassSampleInstance[A]: SampleInstance[A] = ???
/* idea:
    - build upon smaller parts
    - use HList to generalize any type

      String
         Int    ->    HList     ->        A
        (...)                 Generic

   we need:
      SampleInstance[HList]
         SampleInstance[HNil]
         SampleInstance[String]
         SampleInstance[Int]
         SampleInstance[...]
      Generic[A]
 */


























def caseClassSampleInstance1[A, Gen <: HList](implicit
                                       e: SampleInstance[Gen],  //we'll define next
                                       gen: Generic.Aux[A, Gen] //shapeless creates for us
                                      ): SampleInstance[A] = ???

def hlistSampleInstance[Head, Tail <: HList]: SampleInstance[Head :: Tail] = ???
//SampleInstance[Int :: String :: HNil].apply == 1 :: "bla" :: HNil

/* idea: recursive definition
   SampleInstance[Head :: Tail]
     SampleInstance[Head]
     SampleInstance[Tail]

   i.e.
   SampleInstance[Int :: String :: HNil]
    SampleInstance[Int]
    SampleInstance[String :: HNil]
      SampleInstance[String]
      SampleInstance[HNil]
 */

















implicit def hnilSampleInstance: SampleInstance[HNil] = SampleInstance.instance(HNil) //base case for the recursion
implicit def hlistSampleInstance1[Head, Tail <: HList](implicit
        head: SampleInstance[Head],   //some SampleInstance we've defined before: String, Int, ...
        tail: SampleInstance[Tail]    //recursion. last is HNil
       ): SampleInstance[Head :: Tail] = {
  val h: Head = head.get
  val t: Tail = tail.get
  SampleInstance.instance(h :: t)
}
SampleInstance[Int :: String :: HNil].get
  SampleInstance[Int]
  SampleInstance[String :: HNil]
    SampleInstance[String]
    SampleInstance[HNil]

implicit def caseClassSampleInstance2[A, Gen <: HList](implicit
        //*order matters*
        gen: Generic.Aux[A, Gen], //shapeless creates for us.
        e: Lazy[SampleInstance[Gen]]     //we've just defined SampleInstance[HList]
                                  // *lazy needed so compiler doesn't give up the implicit search on complex cases*
       ): SampleInstance[A] = {
  val hlist: Gen = e.value.get
  val a: A = gen.from(hlist)
  SampleInstance.instance(a)
}


//debug
SampleInstance[Person].get
  Generic[Person]
  SampleInstance[String :: Int :: HNil]
    SampleInstance[String]
    SampleInstance[Int]
    SampleInstance[HNil]


//it works!
implicit val bool: SampleInstance[Boolean] = SampleInstance.instance(true)
case class IceCream(flavor: String, numCherries: Int, hasChocolateSauce: Boolean)
sample[IceCream]
//*the next one only works with Lazy
case class Gelateria(owner: Person, iceCream: IceCream, isOpen: Boolean)
sample[Gelateria]



