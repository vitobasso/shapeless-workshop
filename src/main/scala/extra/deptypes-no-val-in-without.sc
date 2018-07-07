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

trait TypeClass[In, Out] {
  def apply(): Out
}

object TypeClass {
  def instance[In, Out](x: Out): TypeClass[In, Out] = new TypeClass[In, Out] {
    override def apply(): Out = x
  }

  def apply[In, Out](implicit tc: TypeClass[In, Out]): TypeClass[In, Out] = tc

  implicit val makeInstance1: TypeClass[Object1.T, Int] = instance(1)
  implicit val makeInstance2: TypeClass[Object2.T, String] = instance("a")
}

def doIt[In, Out](implicit tc: TypeClass[In, Out]): Out = tc.apply()

doIt[Object1.T, Int] //must specify the output type
doIt[Object2.T, String]