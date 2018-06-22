import language.experimental.macros, magnolia._

trait Show[A] {
  def show(a: A): String
}
object Show {
  type Typeclass[A] = Show[A]
  def combine[A](ctx: CaseClass[Show, A]): Show[A] = new Show[A] {
    def show(value: A): String = ctx.parameters.map { p =>
      s"${p.label}=${p.typeclass.show(p.dereference(value))}"
    }.mkString("{", ",", "}")
  }
  def dispatch[A](ctx: SealedTrait[Show, A]): Show[A] =
    new Show[A] {
      def show(value: A): String = ctx.dispatch(value) { sub =>
        sub.typeclass.show(sub.cast(value))
      }
    }
  implicit def gen[A]: Show[A] = macro Magnolia.gen[A]

  def impl[A](f: A => String): Show[A] = new Show[A] {
    override def show(a: A) = f(a)
  }
  implicit val string: Show[String] = impl(identity)
  implicit val int: Show[Int] = impl(_.toString)
}
implicit class ShowOps[A: Show](underlying: A) {
  def show: String = implicitly[Show[A]].show(underlying)
}



sealed trait Tree[+T]
case class Branch[+T](left: Tree[T], right: Tree[T]) extends Tree[T]
case class Leaf[+T](value: T) extends Tree[T]
Branch(Branch(Leaf(1), Leaf(2)), Leaf(3)).show

case class Cat(name: String, livesLeft: Int)
Cat("Jynks", 7).show
