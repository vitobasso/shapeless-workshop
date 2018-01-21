
//phantom type
trait Lasers
val a: Int with Lasers = 123.asInstanceOf[Int with Lasers]

//singleton type
import shapeless.syntax.singleton._
123.narrow
//type "Int(123)" represented internally but no syntax to express it

//type tagging
val c = "bla" ->> 123
import shapeless.tag._
val d: Int @@ Lasers = 123.asInstanceOf[Int @@ Lasers]

//witness
import shapeless.Witness
val e = Witness(123)
//val f: e.T = 123.narrow
//val g: Witness.`123`.T = 123.narrow