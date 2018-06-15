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
  implicit val userId = constr[UserId](str => Try(java.util.UUID.fromString(str)).toOption.map(UserId), _.uuid.toString)
  implicit val bsn = constr[BSN](str => Try(str.toInt).map(BSN(_)).toOption, _.value.toString)
  implicit val iban = constr[IBAN](str => Some(IBAN(str)), _.value)
}

//using the summoner
val instance = CanParse[BSN]
instance.parse("123")
