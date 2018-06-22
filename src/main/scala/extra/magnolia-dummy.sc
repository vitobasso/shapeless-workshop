import magnolia._

import scala.language.experimental.macros


trait Dummy[A] {
  def dummy: A
}
object Dummy { //FIXME
  type Typeclass[A] = Dummy[A]
  def combine[A](ctx: CaseClass[Dummy, A]): Dummy[A] = {
    val res: A = ctx.construct{ _.typeclass.dummy }
    impl(res)
  }
  def dispatch[A](ctx: SealedTrait[Dummy, A]): Dummy[A] = {
    val res: A = ctx.subtypes.map{ _.typeclass.dummy }
    impl(res)
  }
  implicit def gen[A]: Dummy[A] = macro Magnolia.gen[A]

  def impl[A](v: A): Dummy[A] = new Dummy[A] {
    override def dummy: A = v
  }
  implicit val string: Dummy[String] = impl("bla")
  implicit val int: Dummy[Int] = impl(123)
}
implicit def dummy[A]: A = implicitly[Dummy[A]].dummy

//trait Dummies[A] {
//  def dummies: Seq[A]
//}
//object Dummies {
//  type Typeclass[A] = Dummies[A]
//  def combine[A](ctx: CaseClass[Dummies, A]): Dummies[A] =
//    impl(ctx.construct(_.typeclass.dummies))
//  def dispatch[T](ctx: SealedTrait[Dummies, T]): Dummies[T] = {
//    impl(ctx.subtypes.map{ _.typeclass.dummies })
//  }
//  implicit def gen[A]: Dummies[A] = macro Magnolia.gen[A]
//  def impl[A](v: Seq[A]): Dummies[A] = new Dummies[A] {
//    override def dummies: Seq[A] = v
//  }
//}


//sealed trait Tree[+T]
//case class Branch[+T](left: Tree[T], right: Tree[T]) extends Tree[T]
//case class Leaf[+T](value: T) extends Tree[T]
//Branch(Branch(Leaf(1), Leaf(2)), Leaf(3))
//
//dummy[Tree[Int]]

case class Cat(name: String, livesLeft: Int)
//dummy[Cat]
Dummy.gen[Cat]