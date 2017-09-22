

//dependent types
trait DepValue {
  type V
  val value: V
}
def magic(that: DepValue): that.V = that.value
def mk[T](x: T) = new DepValue {
  type V = T
  val value = x
}
val depInt = mk(1)
val depString = mk("two")
val itWorks: Int = magic(depInt)
val again: String = magic(depString)