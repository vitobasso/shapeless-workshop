import shapeless._
case class Person(name: String, age: Int)

//type class
trait Example[A] {
  def apply: A
}

object Example {

  //summoner
  def apply[A](implicit e: Example[A]): Example[A] = e

  //constructor
  def instance[A](v: A): Example[A] = new Example[A] {
    override def apply: A = v
  }

}

//type class instances
implicit val strExample = Example.instance("bla")
implicit val intExample = Example.instance(1)
Example[String].apply
Example[Int].apply


//Example[Person] ?
def personExample: Example[Person] = ???
//idea: given the parts, build the whole
//   String
//             ->    String :: Int     ->        Person
//      Int                          Generic
//
//we need:
//   Example[String]
//   Example[Int]
//   Generic[Person]
def personExample1(implicit
                    str: Example[String], // we've defined before
                    int: Example[Int],    // we've defined before
                    gen: Generic[Person]  // shapeless creates for us
                  ): Example[Person] = ???
//          lemma pattern ^
/* Wikipedia
 *    Lemma:
 *    A "helping theorem" is a proved proposition which is used as a stepping stone to a larger result rather than as a statement of interest by itself.
 *    The word derives from the Ancient Greek λῆμμα ("anything which is received, such as a gift, profit, or a bribe").
 */
type GenPerson = String :: Int :: HList
implicit def personExample2(implicit
                           str: Example[String],
                           int: Example[Int],
                           gen: Generic.Aux[Person, GenPerson],
                          ): Example[Person] = {
  val s: String = str.apply
  val i: Int    = int.apply
  val hlist: GenPerson = s :: i :: HNil
  val p: Person = gen.from(hlist)
  Example.instance(p)
}
Example[Person].apply

//Example[A] ?
//type class derivation
implicit def caseClassExample[A]: Example[A] = ???
//idea: given the parts, build the whole
//   String
//      Int    ->    HList     ->        A
//     (...)                 Generic
//
//we need:
//   Example[HList]
//      Example[String]
//      Example[Int]
//      Example[...]
//   Generic[A]
implicit def caseClassExample1[A, Gen](implicit
                                       e: Example[HList],        //we'll define next
                                       gen: Generic.Aux[A, Gen], //shapeless creates for us
                                      ): Example[A] = ???

implicit def hlistExample[H, T <: HList]: Example[H :: T] = ???
//Example[Int :: String :: HNil].apply == 1 :: "bla" :: HNil

implicit def hnilExample: Example[HNil] = Example.instance(HNil) //base case for the recursion
implicit def hlistExample1[H, T <: HList](implicit
                                          head: Example[H],
                                          tail: Example[T]   //recursion
                                         ): Example[H :: T] = {
  val h: H = head.apply
  val t: T = tail.apply
  Example.instance(h :: t)
}
Example[Int :: String :: HNil].apply






