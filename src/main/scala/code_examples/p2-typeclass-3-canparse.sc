import java.util.UUID
import scala.util.Try
case class PartyUUID(uuid: java.util.UUID)
case class RGB(value: Long)
case class IBAN(value: String)


//type class
trait CanParse[A] {
  def parse(str: String): Option[A]
  def serialize(a: A): String
}

object CanParse {

  //type class instances

  implicit val int = new CanParse[Int] {
    override def serialize(a: Int): String = a.toString
    override def parse(str: String): Option[Int] = Try(str.toInt).toOption
  }

  implicit val partyUuid = new CanParse[PartyUUID] {
    override def parse(str: String): Option[PartyUUID] =
      Try(UUID.fromString(str)).toOption.map(PartyUUID)
    override def serialize(a: PartyUUID) = a.uuid.toString
  }

  implicit val rgb = new CanParse[RGB] {
    override def parse(str: String): Option[RGB] = Try(str.toInt).map(RGB(_)).toOption
    override def serialize(a: RGB) = a.value.toString
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
RGB(123).serialize
parse[PartyUUID]("152f1b99-b6f8-4737-85a0-b232e669c20d")
parse[RGB]("invalid")



//creating generic code on top of it

def encrypt[A: CanParse](a: A): String =
  a.serialize.map(Character.reverseBytes)
def decrypt[A: CanParse](str: String): Option[A] =
  parse[A](str.map(Character.reverseBytes))

val eRgb = encrypt(RGB(123))
val eIban = encrypt(IBAN("8709827342"))
decrypt[RGB](eRgb)
decrypt[IBAN](eIban)
