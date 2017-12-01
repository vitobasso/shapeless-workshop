package extra

import shapeless._
import scala.annotation.implicitNotFound

object SampleInstances {

  /**
    * Defines a "sample" for:
    *   - a basic type (a dummy value)
    *   - a case class (a sample instance with dummy values for fields)
    */
  trait OneSample[A] {
    def apply: A
  }

  object OneSample {

    def apply[A](implicit e: OneSample[A]): OneSample[A] = e
    def instance[A](v: A): OneSample[A] = new OneSample[A] {
      override def apply: A = v
    }

    // default values for basic types
    implicit def int: OneSample[Int]       = instance(0)
    implicit def double: OneSample[Double] = instance(0)
    implicit def string: OneSample[String] = instance("")
    implicit def option[B: OneSample]: OneSample[Option[B]] =
      instance(Some(OneSample[B].apply))

    // sample generation for any case class (that has a OneSample for every field)
    implicit def anyCaseClass[A, Gen](implicit
                                      generic: Generic.Aux[A, Gen],
                                      rSample: Lazy[OneSample[Gen]]
                                     ): OneSample[A] = {
      val gen: Gen = rSample.value.apply
      val res: A   = generic.from(gen)
      instance(res)
    }
    implicit def hnil: OneSample[HNil] = instance(HNil)
    implicit def hlist[H, T <: HList](implicit
                                      hInstance: Lazy[OneSample[H]],
                                      tInstance: OneSample[T]
                                     ): OneSample[H :: T] =
      instance {
        val head: H = hInstance.value.apply
        val tail: T = tInstance.apply
        head :: tail
      }

  }

  /**
    * Generates a Seq of "sample" instances for:
    *   - the hierarchy of a sealed trait
    *   - a case class having multiple samples for one or more of it's fields
    *   - anything having a [[OneSample]]
    */
  @implicitNotFound(
    """Implicit not found for SampleInstances[${A}].
     Possible causes:
      1. A new type was introduced in a field of ${A} or one of it's subclasses.
        E.g.: A case class has a Boolean field, but there's no implicit OneSample for Boolean in scope.
      2. ${A} doesn't conform to shapeless.Generic's restrictions. E.g.:
        - A trait isn't sealed
        - A class isn't case class
        - A trait has no concrete descendants
  """)
  trait SampleInstances[A] {
    def apply: Seq[A]
  }

  object SampleInstances extends LowPrioImplicits {

    def apply[A](implicit e: SampleInstances[A]): SampleInstances[A] = e
    def instance[A](v: Seq[A]): SampleInstances[A] = new SampleInstances[A] {
      override def apply: Seq[A] = v
    }

    implicit def fromUnique[A](implicit unique: OneSample[A]): SampleInstances[A] = {
      val res: A = unique.apply
      instance(Seq(res))
    }

    implicit def anySealedTrait[A, Gen](implicit
                                        generic: Generic.Aux[A, Gen],
                                        rInstance: Lazy[CoproductSamples.Aux[Gen, A]]
                                       ): SampleInstances[A] =
      instance(rInstance.value.apply)
    implicit def hnil: SampleInstances[HNil] = instance(Seq(HNil))
    implicit def hlist[H, T <: HList](implicit hSamples: Lazy[SampleInstances[H]],
                                      tSamples: SampleInstances[T]): SampleInstances[H :: T] =
      instance {
        for {
          head <- hSamples.value.apply
          tail <- tSamples.apply
        } yield head :: tail
      }

  }

  trait LowPrioImplicits {

    /** favor [[SampleInstances.fromUnique]] */
    implicit def anyCaseClass[A, Gen](implicit
                                      generic: Generic.Aux[A, Gen],
                                      rInstance: Lazy[SampleInstances[Gen]]
                                     ): SampleInstances[A] = {
      val gens: Seq[Gen] = rInstance.value.apply
      val res: Seq[A]    = gens.map(generic.from)
      SampleInstances.instance(res)
    }
  }

  /**
    * Serves as intermediate step for [[SampleInstances]]
    * Generates a list of sample instances for a Coproduct
    */
  trait CoproductSamples[A] {
    type R //dependent type R so that a Coproduct B :+: C can return Seq[R] where R is a common supertype
    def apply: Seq[R]
  }

  object CoproductSamples {

    type Aux[A, R0] = CoproductSamples[A] { type R = R0 }
    def apply[A](implicit e: CoproductSamples.Aux[A, A]): CoproductSamples.Aux[A, A] = e
    def instance[A, R0](v: Seq[R0]): CoproductSamples.Aux[A, R0] = new CoproductSamples[A] {
      type R = R0
      override def apply: Seq[R] = v
    }

    implicit def cnil[Anything]: CoproductSamples.Aux[CNil, Anything] = instance(Nil)
    implicit def coproduct[Super, H <: Super, T <: Coproduct](implicit
                                                              hInstance: Lazy[SampleInstances[H]],
                                                              tInstance: CoproductSamples.Aux[T, Super]
                                                             ): CoproductSamples.Aux[H :+: T, Super] = {
      val h: Seq[H]       = hInstance.value.apply
      val t: Seq[Super]   = tInstance.apply
      val res: Seq[Super] = h ++ t
      instance(res)
    }

  }

  //syntax
  def sample[A](implicit s: SampleInstances[A]): Seq[A] = s.apply

  /**
    * Test the [[SampleInstances]] type class and friends
    * Failure means something is wrong in the type class machinery rather than missing a [[OneSample]] instance
    * for a basic type used
    */
  object GeneratorTest {

    sealed trait Trait
    case object Singleton           extends Trait
    case class CaseClass(s: String) extends Trait
    sealed trait TraitWithNesting
    case class CaseClassWithNesting(nested: Trait) extends TraitWithNesting

    OneSample[Singleton.type]
    OneSample[CaseClass]
    SampleInstances[CaseClass]
    SampleInstances[Trait]
    SampleInstances[CaseClassWithNesting]
    SampleInstances[TraitWithNesting]
  }

}
