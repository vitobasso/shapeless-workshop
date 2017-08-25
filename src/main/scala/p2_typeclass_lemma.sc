import shapeless._

//dependency from previous worksheet
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

// ???
def example[A](implicit e: Example[A]): A = e.apply


//type class instances
implicit val strExample: Example[String] = Example.instance("bla")
implicit val intExample: Example[Int] = Example.instance(1)
Example[String].apply
Example[Int].apply


//it's all just sugar

//summoner
Example[Int]
implicitly[Example[Int]]

//constructor
Example.instance(1)
new Example[Int]{ override def apply = 1 }

//???
example[Int]
Example[Int].apply



//Example[Person] ?
def personExample: Example[Person] = ???
/* idea: given the parts, build the whole
      String
                ->    String :: Int     ->        Person
         Int                          Generic

   we need:
      Example[String]
      Example[Int]
      Generic[Person]
 */
def personExample1(implicit
                    str: Example[String], // we've defined before
                    int: Example[Int],    // we've defined before
                    gen: Generic[Person]  // shapeless creates for us
                  ): Example[Person] = ???
//          lemma pattern ^
/* Wikipedia
      Lemma:
      A "helping theorem" is a proved proposition which is used as a stepping stone to a larger result rather than as a statement of interest by itself.
      The word derives from the Ancient Greek λῆμμα ("anything which is received, such as a gift, profit, or a bribe").
 */
type GenPerson = String :: Int :: HNil
implicit def personExample2(implicit
                           str: Example[String],
                           int: Example[Int],
                           gen: Generic.Aux[Person, GenPerson]
                          ): Example[Person] = {
  // "proof"
  val s: String = str.apply
  val i: Int    = int.apply
  val hlist: GenPerson = s :: i :: HNil
  val p: Person = gen.from(hlist)
  Example.instance(p)
}
//to debug, break down into parts
Example[Person].apply
  Example[String]
  Example[Int]
  Generic[Person]
