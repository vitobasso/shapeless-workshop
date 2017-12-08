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
  derive SampleInstance[Person] from what we have:
    - SampleInstance[String]
    - SampleInstance[Int]
    - Generic[Person]

  goal: sample[Person] == Person(bla,1)
 */

//SampleInstance[Person] ?
def personSampleInstance: SampleInstance[Person] = ???



























def personSampleInstance1(implicit
                    str: SampleInstance[String], // we've defined before
                    int: SampleInstance[Int],    // we've defined before
                    gen: Generic[Person]  // shapeless creates for us
                  ): SampleInstance[Person] = ???
//          lemma pattern ^









type GenPerson = String :: Int :: HNil
implicit def personSampleInstance2(implicit
                           str: SampleInstance[String],
                           int: SampleInstance[Int],
                           gen: Generic.Aux[Person, GenPerson]
                          ): SampleInstance[Person] = {
  // "proof"
  val s: String = str.get
  val i: Int    = int.get
  val hlist: GenPerson = s :: i :: HNil
  val p: Person = gen.from(hlist)
  SampleInstance.instance(p)
}

//to debug, break down into parts
SampleInstance[Person]
  Generic[Person]
  SampleInstance[String]
  SampleInstance[Int]


//it works!
sample[Person]