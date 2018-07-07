/*
a typeclass that takes as input:
  - a type
  - no value
and produces:
  - a new type
  - a value of this type

https://stackoverflow.com/questions/51131067/when-are-dependent-types-needed-in-shapeless/51144080?noredirect=1#comment89343934_51144080
 */

trait MyTrait {
  type T
}

object Object1 extends MyTrait
object Object2 extends MyTrait

trait TypeClass[In] {
  type Out
  def apply(): Out
}

object TypeClass {
  type Aux[In, Out0] = TypeClass[In] { type Out = Out0 }
  def instance[In, Out0](x: Out0): Aux[In, Out0] = new TypeClass[In] {
    override type Out = Out0
    override def apply(): Out = x
  }

  def apply[In](implicit tc: TypeClass[In]): Aux[In, tc.Out] = tc

  implicit val makeInstance1: Aux[Object1.T, Int] = instance(1)
  implicit val makeInstance2: Aux[Object2.T, String] = instance("a")
}

def doIt[A](implicit tc: TypeClass[A]): tc.Out = tc.apply()

doIt[Object1.T]
doIt[Object2.T]