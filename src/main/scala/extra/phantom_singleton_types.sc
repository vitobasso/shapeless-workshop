
//phantom type
trait Lasers
val a: Int with Lasers = 123.asInstanceOf[Int with Lasers]

//singleton type
import shapeless.syntax.singleton._
123.narrow

//type tagging
val c = "bla" ->> 123
import shapeless.tag._
val d: Int @@ Lasers = 123.asInstanceOf[Int @@ Lasers]

//witness
import shapeless.Witness
val e: Witness.Lt[Int] = Witness(123)
e.value