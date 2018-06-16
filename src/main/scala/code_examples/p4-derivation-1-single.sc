/*
  Deriving type class instances
  Let's start small: case classes with only one field.
 */

//To call `sum` on a Seq[A] we need a Numeric[A]
case class Cost(value: Int)
//Seq(Cost(1), Cost(2), Cost(3)).sum // no Numeric[Cost] :/

//We could create one specifically for Cost
implicit val costIsNumeric: Numeric[Cost] =
  new Numeric[Cost] {
    override def plus(x: Cost, y: Cost) = Cost(x.value + y.value)
    override def toInt(x: Cost) = x.value
    override def fromInt(x: Int) = Cost(x)

    //the other methods don't matter for `sum`
    override def minus(x: Cost, y: Cost) = ???
    override def times(x: Cost, y: Cost) = ???
    override def negate(x: Cost) = ???
    override def toLong(x: Cost) = ???
    override def toFloat(x: Cost) = ???
    override def toDouble(x: Cost) = ???
    override def compare(x: Cost, y: Cost) = ???
  }

Seq(Cost(1), Cost(2), Cost(3)).sum



//Oh no, a new wrapper :~
case class Weight(value: Double)
//Seq(Weight(1), Weight(2), Weight(3)).sum // no Numeric[Weight] :/

//Derivation: will work for Cost, Weight, etc
import shapeless._
implicit def anyWrapperIsNumeric[A, N](implicit
                                         lazyGen: Lazy[Generic.Aux[A, N :: HNil]],
                                         num: Numeric[N]
                                       //note:
                                       //   - order matters
                                       //   - lazy to workaround "implicit divergence"
                                         ): Numeric[A] =
  new Numeric[A] {
    private val gen = lazyGen.value

    override def plus(x: A, y: A): A = {
      val a: N = gen.to(x).head
      val b: N = gen.to(y).head
      val sum: N = num.plus(a, b)
      val hlist: N :: HNil = sum :: HNil
      val result: A = gen.from(hlist)
      result
    }
    override def toInt(x: A) = num.toInt(gen.to(x).head)
    override def fromInt(x: Int) = gen.from(num.fromInt(x) :: HNil)

    //the other methods don't matter for `sum`
    override def toLong(x: A) = ???
    override def minus(x: A, y: A) = ???
    override def times(x: A, y: A) = ???
    override def negate(x: A) = ???
    override def toFloat(x: A) = ???
    override def toDouble(x: A) = ???
    override def compare(x: A, y: A) = ???
  }

Seq(Weight(1), Weight(2), Weight(3)).sum