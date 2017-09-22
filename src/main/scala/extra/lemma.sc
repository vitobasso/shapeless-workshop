import java.io.OutputStream


//some type classes
trait JsonEncoder[A] {
  def encode(v: A): String
}
trait Writes[A] {
  def write(dest: java.io.OutputStream, v: A): Unit
}

//composition (lemma pattern)
def generateWriter[A](implicit
                               outStr: Writes[String],
                               jsonEnc: JsonEncoder[A]
                              ): Writes[A] =
  new Writes[A] {
    override def write(dest: OutputStream, v: A): Unit =
      outStr.write(dest, jsonEnc.encode(v))
  }

//some types
case class Person(name: String, age: Int)
case class Aeroplane(airline: String, passengers: Int)

//some type class instances
implicit val personEncoder: JsonEncoder[Person] =
  (v: Person) => s"""{"name":${v.name}, "age":${v.age}"""
implicit val stringWriter: Writes[String] =
  (dest: OutputStream, v: String) => dest.write(v.getBytes)

//derivation
val personWriter: Writes[Person] = generateWriter[Person] //compiles
val aeroplaneWriter: Writes[Aeroplane] = generateWriter[Aeroplane] //does not compiles
