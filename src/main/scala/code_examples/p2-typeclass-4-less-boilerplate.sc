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

  //summoner
  def apply[A](implicit instance: CanParse[A]): CanParse[A] = instance

  //constructor
  def constr[A](howToParse: String => Option[A], howToSerialize: A => String): CanParse[A] =
    new CanParse[A] {
      override def serialize(a: A): String = howToSerialize(a)
      override def parse(str: String): Option[A] = howToParse(str)
    }

  //type class instances (using the constructor)
  implicit val int = constr[Int](str => Try(str.toInt).toOption, _.toString)
  implicit val partyUUID = constr[PartyUUID](str => Try(UUID.fromString(str)).toOption.map(PartyUUID), _.uuid.toString)
  implicit val rgb = constr[RGB](str => Try(str.toInt).map(RGB(_)).toOption, _.value.toString)
  implicit val iban = constr[IBAN](str => Some(IBAN(str)), _.value)
}

//using the summoner
CanParse[RGB].parse("123456")

