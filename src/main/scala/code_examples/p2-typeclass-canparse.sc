import scala.util.Try

trait CanParse[A] {
  def parse(str: String): Option[A]
  def raw(a: A): String
}
implicit val canParseInt: CanParse[Int] = new CanParse[Int] {
  override def raw(a: Int): String = a.toString
  override def parse(str: String): Option[Int] = Try(str.toInt).toOption
}

//syntax sugar
implicit class CanParseOps[A: CanParse](underlying: A) {
  def raw: String = implicitly[CanParse[A]].raw(underlying)
}
1.raw

case class PartyUUID(uuid: java.util.UUID)
case class RGB(value: Long)
case class IBAN(value: String)

implicit val canParsePartyUUID = new CanParse[PartyUUID] {
  override def parse(str: String): Option[PartyUUID] =
    Try(java.util.UUID.fromString(str)).toOption.map(PartyUUID)
  override def raw(a: PartyUUID) = a.uuid.toString
}
implicit val canParseRGB = new CanParse[RGB] {
  override def parse(str: String): Option[RGB] = Try(str.toInt).map(RGB(_)).toOption
  override def raw(a: RGB) = a.value.toString
}
implicit val canParseIBAN = new CanParse[IBAN] {
  override def parse(str: String): Option[IBAN] = Some(IBAN(str))
  override def raw(a: IBAN) = a.value
}
def parse[A: CanParse](str: String): Option[A] =
  implicitly[CanParse[A]].parse(str)
parse[PartyUUID]("152f1b99-b6f8-4737-85a0-b232e669c20d")

//abstract over things that CanParse from String
def encrypt[A: CanParse](a: A): String =
  a.raw.map(Character.reverseBytes)
encrypt(RGB(123))
encrypt(IBAN("8709827342"))
