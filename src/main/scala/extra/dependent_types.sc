

//dependent types
trait Dep {
  type V
  val value: V
}
def mk[T](x: T) = new Dep {
  type V = T
  val value = x
}
val depInt: Dep { type V = Int } = mk(1)
val depString: Dep { type V = String } = mk("two")

def magic(dep: Dep): dep.V = dep.value
val iKnowTheType: Int = magic(depInt)
val again: String = magic(depString)


//aux
object Dep {
  type Aux[V0] = Dep{ type V = V0 }
}
val depInt2: Dep.Aux[Int] = mk(1)
val depString2: Dep.Aux[String] = mk("two")
