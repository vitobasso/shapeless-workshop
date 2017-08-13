package extra

import shapeless._
import scala.annotation.implicitNotFound

object GenerateExamples {

  /**
    * Defines an "example" for:
    *   - a basic type (a dummy value)
    *   - a case class (an example instance with dummy values for fields)
    */
  trait UniqueExample[A] {
    def apply: A
  }

  object UniqueExample {

    def apply[A](implicit e: UniqueExample[A]): UniqueExample[A] = e
    def instance[A](v: A): UniqueExample[A] = new UniqueExample[A] {
      override def apply: A = v
    }

    // default values for basic types
    implicit def int: UniqueExample[Int]       = instance(0)
    implicit def double: UniqueExample[Double] = instance(0)
    implicit def string: UniqueExample[String] = instance("")
    implicit def option[B: UniqueExample]: UniqueExample[Option[B]] =
      instance(Some(UniqueExample[B].apply))

    // example generation for any case class (that has a UniqueExample for every field)
    implicit def anyCaseClass[A, Gen](implicit
                                      generic: Generic.Aux[A, Gen],
                                      rExample: Lazy[UniqueExample[Gen]]
                                     ): UniqueExample[A] = {
      val gen: Gen = rExample.value.apply
      val res: A   = generic.from(gen)
      instance(res)
    }
    implicit def hnil: UniqueExample[HNil] = instance(HNil)
    implicit def hlist[H, T <: HList](implicit
                                      hInstance: Lazy[UniqueExample[H]],
                                      tInstance: UniqueExample[T]
                                     ): UniqueExample[H :: T] =
      instance {
        val head: H = hInstance.value.apply
        val tail: T = tInstance.apply
        head :: tail
      }

  }

  /**
    * Generates a Seq of "example" instances for:
    *   - the hierarchy of a sealed trait
    *   - a case class having multiple examples for one or more of it's fields
    *   - anything having an [[UniqueExample]]
    */
  @implicitNotFound(
    """Implicit not found for GenerateExamples[${A}].
     Possible causes:
      1. A new type was introduced in a field of ${A} or one of it's subclasses.
        E.g.: A case class has a Boolean field, but there's no implicit UniqueExample for Boolean in scope.
      2. ${A} doesn't conform to shapeless.Generic's restrictions. E.g.:
        - A trait isn't sealed
        - A class isn't case class
        - A trait has no concrete descendants
  """)
  trait GenerateExamples[A] {
    def apply: Seq[A]
  }

  object GenerateExamples extends LowPrioImplicits {

    def apply[A](implicit e: GenerateExamples[A]): GenerateExamples[A] = e
    def instance[A](v: Seq[A]): GenerateExamples[A] = new GenerateExamples[A] {
      override def apply: Seq[A] = v
    }

    implicit def fromUnique[A](implicit unique: UniqueExample[A]): GenerateExamples[A] = {
      val res: A = unique.apply
      instance(Seq(res))
    }

    implicit def anySealedTrait[A, Gen](implicit
                                        generic: Generic.Aux[A, Gen],
                                        rInstance: Lazy[CoproductExamples.Aux[Gen, A]]
                                       ): GenerateExamples[A] =
      instance(rInstance.value.apply)
    implicit def hnil: GenerateExamples[HNil] = instance(Seq(HNil))
    implicit def hlist[H, T <: HList](implicit hExamples: Lazy[GenerateExamples[H]],
                                      tExamples: GenerateExamples[T]): GenerateExamples[H :: T] =
      instance {
        for {
          head <- hExamples.value.apply
          tail <- tExamples.apply
        } yield head :: tail
      }

  }

  trait LowPrioImplicits {

    /** favor [[GenerateExamples.fromUnique]] */
    implicit def anyCaseClass[A, Gen](implicit
                                      generic: Generic.Aux[A, Gen],
                                      rInstance: Lazy[GenerateExamples[Gen]]
                                     ): GenerateExamples[A] = {
      val gens: Seq[Gen] = rInstance.value.apply
      val res: Seq[A]    = gens.map(generic.from)
      GenerateExamples.instance(res)
    }
  }

  /**
    * Serves as intermediate step for [[GenerateExamples]]
    * Generates a list of example instances for a Coproduct
    */
  trait CoproductExamples[A] {
    type R //dependent type R so that a Coproduct B :+: C can return Seq[R] where R is a common supertype
    def apply: Seq[R]
  }

  object CoproductExamples {

    type Aux[A, R0] = CoproductExamples[A] { type R = R0 }
    def apply[A](implicit e: CoproductExamples.Aux[A, A]): CoproductExamples.Aux[A, A] = e
    def instance[A, R0](v: Seq[R0]): CoproductExamples.Aux[A, R0] = new CoproductExamples[A] {
      type R = R0
      override def apply: Seq[R] = v
    }

    implicit def cnil[Anything]: CoproductExamples.Aux[CNil, Anything] = instance(Nil)
    implicit def coproduct[Super, H <: Super, T <: Coproduct](implicit
                                                              hInstance: Lazy[GenerateExamples[H]],
                                                              tInstance: CoproductExamples.Aux[T, Super]
                                                             ): CoproductExamples.Aux[H :+: T, Super] = {
      val h: Seq[H]       = hInstance.value.apply
      val t: Seq[Super]   = tInstance.apply
      val res: Seq[Super] = h ++ t
      instance(res)
    }

  }

  /**
    * Test the [[GenerateExamples]] type class and friends
    * Failure means something is wrong in the type class machinery rather than missing a [[UniqueExample]] instance
    * for a basic type used
    */
  object GeneratorTest {

    sealed trait Trait
    case object Singleton           extends Trait
    case class CaseClass(s: String) extends Trait
    sealed trait TraitWithNesting
    case class CaseClassWithNesting(nested: Trait) extends TraitWithNesting

    UniqueExample[Singleton.type]
    UniqueExample[CaseClass]
    GenerateExamples[CaseClass]
    GenerateExamples[Trait]
    GenerateExamples[CaseClassWithNesting]
    GenerateExamples[TraitWithNesting]
  }

}
