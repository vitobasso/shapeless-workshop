import scala.language.higherKinds
//https://stackoverflow.com/questions/28287612/how-to-require-typesafe-constant-size-array-in-scala

type True = True.type
type False = False.type

sealed trait Bool {
  type && [B <: Bool] <: Bool
  type || [B <: Bool] <: Bool
  type IfElse[B, T <: B, F <: B] <: B
}
object True extends Bool {
  type && [B <: Bool] = B
  type || [B <: Bool] = True
  type IfElse[B, T <: B, F <: B] = T
}
object False extends Bool {
  type && [B <: Bool] = False
  type || [B <: Bool] = B
  type IfElse[B, T <: B, F <: B] = F
}


assert ( (False. && (False)) == False )
assert ( (False. && (True )) == False )
assert ( (True. && (False)) == False )
assert ( (True. && (True )) == True )

assert ( (False. || (False)) == False )
assert ( (False. || (True )) == True )
assert ( (True. || (False)) == True )
assert ( (True. || (True )) == True )

assert ( False. ifElse[Int](1, 2) == 2 )
assert ( True. ifElse[Int](1, 2) == 1 )