/*
    1.  create the type class Show[A]
        that does:
          def show(a: A): String

    2.  create basic instances for:
          Int
          String
          Boolean
            - shows "yes" or "no"
          Cat(name: String, livesLeft: Int)
            - shows like "Gatarys, 7"

    3.  create an implicit class to enable:
          123.show == "123"

 */

case class Cat(name: String, livesLeft: Int)

// YOUR CODE GOES HERE

//goal:
123.show                // "123"
"bla bla".show          // "bla bla"
true.show               // "yes"
Cat("Gatarys", 7).show  // "Gatarys, 7"