import magnolia._

import scala.language.experimental.macros

trait Dummy[A] {
  def dummy: A
}
object Dummy { //FIXME
  type Typeclass[A] = Dummy[A]
  def combine[A](ctx: CaseClass[Dummy, A]): Dummy[A] = {
    val res: A = ctx.construct{ param => param.typeclass.dummy }
    impl(res)
  }
  def dispatch[A](ctx: SealedTrait[Dummy, A]): Dummy[A] = {
    val res: Seq[A] = ctx.subtypes.map{ subType => subType.typeclass.dummy }
    impl(res.head)
  }
  implicit def gen[A]: Dummy[A] = macro Magnolia.gen[A]

  def impl[A](v: A): Dummy[A] = new Dummy[A] {
    override def dummy: A = v
  }
  implicit val string: Dummy[String] = impl("bla")
  implicit val int: Dummy[Int] = impl(123)
}
implicit def dummy[A](implicit d: Dummy[A]): A = d.dummy


sealed trait Trait
case object Singleton           extends Trait
case class CaseClazz(s: String) extends Trait
sealed trait TraitWithNesting
case class CaseClassWithNesting(nested: Trait) extends TraitWithNesting
dummy[Singleton.type]
dummy[CaseClazz]
dummy[Trait]
dummy[CaseClassWithNesting]
dummy[TraitWithNesting]


//FIXME doesn't work for a recursive type
//sealed trait Tree[+T]
//case class Branch[+T](left: Tree[T], right: Tree[T]) extends Tree[T]
//case class Leaf[+T](value: T) extends Tree[T]
//Branch(Branch(Leaf(1), Leaf(2)), Leaf(3))
//dummy[Leaf[Int]]
//dummy[Branch[Int]]
//dummy[Tree[Int]]