import shapeless._

//dependency from previous worksheet
case class Person(name: String, age: Int)


//type class
trait Example[A] {
  def get: A
}

object Example {

  //summoner
  def apply[A](implicit e: Example[A]): Example[A] = e

  //constructor
  //  a.k.a pure
  def instance[A](v: A): Example[A] = new Example[A] {
    override def get: A = v
  }

}

//syntax
def example[A](implicit e: Example[A]): A = e.get


//it's all just sugar for:

//summoner
Example[Int]
implicitly[Example[Int]]

//constructor
Example.instance(1)
new Example[Int]{ override def get = 1 }

//syntax
example[Int]
Example[Int].get


//type class instances
implicit val strExample: Example[String] = Example.instance("bla")
implicit val intExample: Example[Int] = Example.instance(1)


//it works!
example[String]
example[Int]
