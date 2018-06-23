import shapeless._
import shapeless.ops.adjoin.Adjoin


trait Router[A] {
  self =>

  def apply(path: String): Option[A]

  def :+:[B](that: Router[B])(implicit adjoin: Adjoin[B :+: A :+: CNil]): Router[adjoin.Out] =
    new Router[adjoin.Out]{
      override def apply(path: String): Option[adjoin.Out] =
        that(path).map(b => adjoin(Inl(b)))
          .orElse(self(path).map(a => adjoin(Inr(Inl(a)))))
    }

  //without Adjoin, can't compose into a flat coproduct
//  def :+:[B](that: Router[B]): Router[B :+: A :+: CNil] =
//    new Router[B :+: A :+: CNil]{
//      override def apply(path: String): Option[B :+: A :+: CNil] =
//        that(path).map(b => Inl(b))
//          .orElse(self(path).map(a => Inr(Inl(a))))
//    }
}


val routerStr = new Router[String] {
  override def apply(path: String): Option[String] =
    if(path == "str") Some("bla") else None
}
val routerInt = new Router[Int] {
  override def apply(path: String): Option[Int] =
    if(path == "num") Some(123) else None
}
val routerBool = new Router[Boolean] {
  override def apply(path: String): Option[Boolean] =
    if(path == "bool") Some(true) else None
}
val routerStrInt: Router[String :+: Int :+: CNil] = routerStr :+: routerInt

//needs Adjoin to flatten [(A :+: B) :+: C] into [A :+: B :+: C]
val routerAll: Router[String :+: Int :+: Boolean :+: CNil] = routerStrInt :+: routerBool

routerStr("str")
routerInt("num")
routerBool("bool")
routerStrInt("str")
routerStrInt("num")
routerStrInt("other")
routerAll("str")
routerAll("num")
routerAll("bool")
routerAll("other")

//val adjoin = implicitly[Adjoin[String :+: Int :+: CNil]]
//adjoin.apply(Inl("bla"))
//adjoin.apply(Inr(Inl(123)))