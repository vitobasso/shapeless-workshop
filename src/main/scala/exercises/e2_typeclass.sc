/*
  2.
    a.  create the type class EncodeCsv[A]
        that does:
          def encode(a: A): List[String]

    b.  create the sugar methods:
          summoner (apply)
          constructor (instance/pure)
          "syntax" (encode)

    c.  create basic instances for:
          Int
          String
          Boolean (make it return "yes" or "no")

    d.  prove that it works:
          encode(true) should return List("yes")
 */
