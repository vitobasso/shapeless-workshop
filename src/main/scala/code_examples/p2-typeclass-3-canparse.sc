import scala.util.Try

case class UserId(uuid: java.util.UUID)
case class BSN(value: Long)
case class IBAN(value: String)


//type class
trait CanParse[A] {
  def parse(str: String): Option[A]
  def serialize(a: A): String
}

object CanParse {

  //type class instances

  implicit val int = new CanParse[Int] {
    override def parse(str: String): Option[Int] = Try(str.toInt).toOption
    override def serialize(a: Int): String = a.toString
  }

  implicit val userId = new CanParse[UserId] {
    override def parse(str: String): Option[UserId] =
      Try(java.util.UUID.fromString(str)).toOption.map(UserId)
    override def serialize(a: UserId) = a.uuid.toString
  }

  implicit val bsn = new CanParse[BSN] {
    override def parse(str: String): Option[BSN] = Try(str.toInt).map(BSN(_)).toOption
    override def serialize(a: BSN) = a.value.toString
  }

  implicit val iban = new CanParse[IBAN] {
    override def parse(str: String): Option[IBAN] = Some(IBAN(str))
    override def serialize(a: IBAN) = a.value
  }
}

//syntax sugar
implicit class CanParseOps[A: CanParse](underlying: A) {
  def serialize: String = implicitly[CanParse[A]].serialize(underlying)
}
def parse[A: CanParse](str: String): Option[A] = implicitly[CanParse[A]].parse(str)



//usage

123.serialize
BSN(123).serialize
parse[UserId]("152f1b99-b6f8-4737-85a0-b232e669c20d")
parse[BSN]("invalid")



//creating generic code on top of it

def encrypt[A: CanParse](a: A): String =
  a.serialize.map(Character.reverseBytes)
def decrypt[A: CanParse](str: String): Option[A] =
  parse[A](str.map(Character.reverseBytes))

val eBsn = encrypt(BSN(123))
val eIban = encrypt(IBAN("8709827342"))
decrypt[BSN](eBsn)
decrypt[BSN]("a funny string")
decrypt[IBAN](eIban)
