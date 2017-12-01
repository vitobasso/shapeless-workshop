
//type class
trait SampleInstance[A] {
  def get: A
}

object SampleInstance {

  //summoner
  def apply[A](implicit e: SampleInstance[A]): SampleInstance[A] = e

  //constructor
  //  a.k.a pure
  def instance[A](v: A): SampleInstance[A] = new SampleInstance[A] {
    override def get: A = v
  }

}

//syntax
def sample[A](implicit e: SampleInstance[A]): A = e.get


//it's all just sugar for:

//summoner
SampleInstance[Int]
implicitly[SampleInstance[Int]]

//constructor
SampleInstance.instance(1)
new SampleInstance[Int]{ override def get = 1 }

//syntax
sample[Int]
SampleInstance[Int].get


//type class instances
implicit val strInstance: SampleInstance[String] = SampleInstance.instance("bla")
implicit val intInstance: SampleInstance[Int] = SampleInstance.instance(1)


//it works!
sample[String]
sample[Int]


//in the standard scala lib
List(1,2,3).sum
//List("a", "b", "c").sum // doesn't compile
