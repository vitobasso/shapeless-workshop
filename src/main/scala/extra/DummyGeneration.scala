package extra

import shapeless._
import scala.annotation.implicitNotFound

/**
  * Implements generation for the Dummy type class.
  * -
  */
object DummyGeneration {

  /**
    * Defines a "dummy" for:
    *   - a basic type (int, str, boolean, etc)
    *   - a case class (combination of basic types)
    */
  trait SingleDummy[A] {
    def apply: A
  }

  object SingleDummy {

    def apply[A](implicit e: SingleDummy[A]): SingleDummy[A] = e
    def instance[A](v: A): SingleDummy[A] = new SingleDummy[A] {
      override def apply: A = v
    }

    // dummy values for basic types
    implicit val int: SingleDummy[Int]       = instance(0)
    implicit val double: SingleDummy[Double] = instance(0)
    implicit val string: SingleDummy[String] = instance("")
    implicit def option[B: SingleDummy]: SingleDummy[Option[B]] =
      instance(Some(SingleDummy[B].apply))
    implicit def list[B: SingleDummy]: SingleDummy[List[B]] =
      instance(List(SingleDummy[B].apply))

    // dummy generation for any case class (that has a SingleDummy for every field)
    implicit def anyCaseClass[A, Gen](implicit
                                      generic: Generic.Aux[A, Gen],
                                      dummyHlist: Lazy[SingleDummy[Gen]]
                                     ): SingleDummy[A] = {
      val gen: Gen = dummyHlist.value.apply
      val res: A   = generic.from(gen)
      instance(res)
    }
    implicit def hnil: SingleDummy[HNil] = instance(HNil)
    implicit def hlist[H, T <: HList](implicit
                                      head: Lazy[SingleDummy[H]],
                                      tail: SingleDummy[T]
                                     ): SingleDummy[H :: T] =
      instance {
        val headValue: H = head.value.apply
        val tailValue: T = tail.apply
        headValue :: tailValue
      }

  }

  /**
    * Generates a Seq of "dummy" instances for:
    *   - the hierarchy of a sealed trait
    *   - a case class having multiple dummies for one or more of it's fields
    *   - anything having a [[SingleDummy]]
    */
  @implicitNotFound(
    """Implicit not found for Dummy[${A}].
     Possible causes:
      1. A new type was introduced in a field of ${A} or one of it's subclasses.
        E.g.: A case class has a Boolean field, but there's no implicit SingleDummy for Boolean in scope.
      2. ${A} doesn't conform to shapeless.Generic's restrictions. E.g.:
        - A trait isn't sealed
        - A class isn't case class
        - A trait has no concrete descendants
  """)
  trait Dummy[A] {
    def apply: Seq[A]
  }

  object Dummy extends LowPrioImplicits {

    def apply[A](implicit e: Dummy[A]): Dummy[A] = e
    def instance[A](v: Seq[A]): Dummy[A] = new Dummy[A] {
      override def apply: Seq[A] = v
    }

    implicit def fromUnique[A](implicit unique: SingleDummy[A]): Dummy[A] = {
      val res: A = unique.apply
      instance(Seq(res))
    }

    implicit def anySealedTrait[A, Gen](implicit
                                        generic: Generic.Aux[A, Gen],
                                        dummyCoproduct: Lazy[CoproductDummy.Aux[Gen, A]]
                                       ): Dummy[A] =
      instance(dummyCoproduct.value.apply)
    implicit def hnil: Dummy[HNil] = instance(Seq(HNil))
    implicit def hlist[H, T <: HList](implicit head: Lazy[Dummy[H]],
                                      tail: Dummy[T]): Dummy[H :: T] =
      instance {
        for {
          head <- head.value.apply
          tail <- tail.apply
        } yield head :: tail
      }

  }

  trait LowPrioImplicits {

    /** favor [[Dummy.fromUnique]] */
    implicit def anyCaseClass[A, Gen](implicit
                                      generic: Generic.Aux[A, Gen],
                                      dummyHlist: Lazy[Dummy[Gen]]
                                     ): Dummy[A] = {
      val gens: Seq[Gen] = dummyHlist.value.apply
      val res: Seq[A]    = gens.map(generic.from)
      Dummy.instance(res)
    }
  }

  /**
    * Serves as intermediate step for [[Dummy]]
    * Generates a list of dummy instances for a Coproduct
    */
  trait CoproductDummy[A] {
    type R //dependent type R so that a Coproduct B :+: C can return Seq[R] where R is a common supertype
    def apply: Seq[R]
  }

  object CoproductDummy {

    type Aux[A, R0] = CoproductDummy[A] { type R = R0 }
    def apply[A](implicit e: CoproductDummy.Aux[A, A]): CoproductDummy.Aux[A, A] = e
    def instance[A, R0](v: Seq[R0]): CoproductDummy.Aux[A, R0] = new CoproductDummy[A] {
      type R = R0
      override def apply: Seq[R] = v
    }

    implicit def cnil[Anything]: CoproductDummy.Aux[CNil, Anything] = instance(Nil)
    implicit def coproduct[Super, H <: Super, T <: Coproduct](implicit
                                                              head: Lazy[Dummy[H]],
                                                              tail: CoproductDummy.Aux[T, Super]
                                                             ): CoproductDummy.Aux[H :+: T, Super] = {
      val h: Seq[H]       = head.value.apply
      val t: Seq[Super]   = tail.apply
      val res: Seq[Super] = h ++ t
      instance(res)
    }

  }

  //syntax
  def dummies[A](implicit s: Dummy[A]): Seq[A] = s.apply

  /**
    * Test the [[Dummy]] type class and friends
    * Failure means something is wrong in the type class machinery rather than missing a [[SingleDummy]] instance
    * for a basic type being used
    */
  object GeneratorTest {

    sealed trait Trait
    case object Singleton           extends Trait
    case class CaseClass(s: String) extends Trait
    sealed trait TraitWithNesting
    case class CaseClassWithNesting(nested: Trait) extends TraitWithNesting

    SingleDummy[Singleton.type]
    SingleDummy[CaseClass]
    Dummy[CaseClass]
    Dummy[Trait]
    Dummy[CaseClassWithNesting]
    Dummy[TraitWithNesting]
  }

}
