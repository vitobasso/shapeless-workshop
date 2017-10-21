import shapeless._
import shapeless.labelled.FieldType

/*
    http://enear.github.io/2016/09/27/bits-of-shapeless-2/
    https://gist.github.com/milessabin/9042788

    https://blog.scalac.io/2015/05/21/dynamic-member-lookup-in-scala.html
 */

case class Person(name: String, age: Int)

//import shapeless.tag.@@
//val nameWitness = Witness("name")
//type NameTag = Symbol @@ nameWitness.T
type NameTag = Witness.`'name`.T  //singleton/literal types
                                  //dynamic member lookup
type NameField = FieldType[NameTag, String] //phantom types, type tagging
type AgeTag = Witness.`'age`.T
type AgeField = FieldType[AgeTag, Int]
type H = NameField :: AgeField :: HNil

val g = LabelledGeneric[Person]
val h: g.Repr = g.to(Person("bla", 123))
implicitly[g.Repr =:= H]

import record._
val tag: NameTag = h.keys.head
val field: NameField = h.head

def getFieldName[K, V](value: FieldType[K, V])
                      (implicit witness: Witness.Aux[K]): K =
  witness.value

getFieldName(field)

//manually creating Records (aka tagged types)
import shapeless.syntax.singleton._
val p2: H = ('name ->> "bla") :: ('age ->> 123) :: HNil
assert(h == p2)