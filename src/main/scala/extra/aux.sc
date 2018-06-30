//why do we need Aux?
//https://stackoverflow.com/questions/43900674/understanding-the-aux-pattern-in-scala-type-system

trait TypeClass1[In] {
  type Out
}
trait TypeClass2[In] {
  type Out
}
trait TypeClass3[In] {
  type Out
}

//ideal
//doesn't work: can't ref ev1 in the same param list
implicit def derivate1[A](implicit ev1: TypeClass1[A], ev2: TypeClass2[ev1.Out]): TypeClass3[ev2.Out] = ???

//separate arg lists
//doesn't work: can only have one arg list implicit
implicit def derivate2[A](ev1: TypeClass1[A])(ev2: TypeClass2[ev1.Out]): TypeClass3[ev2.Out] = ???

//structural type
//works
implicit def derivate3[A, B](implicit ev1: TypeClass1[A]{ type Out = B }, ev2: TypeClass2[B]): TypeClass3[ev2.Out] = ???

//aux
//works
type TypeClass1_Aux[I,O] = TypeClass1[I]{ type Out = O }
implicit def derivate4[A, B](implicit ev1: TypeClass1_Aux[A,B], ev2: TypeClass2[B]): TypeClass3[ev2.Out] = ???

