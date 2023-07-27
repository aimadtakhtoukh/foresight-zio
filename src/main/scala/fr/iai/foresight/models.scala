package fr.iai.foresight

import fr.iai.foresight.models.User
import java.util.UUID
import zio.json.*

object models {

  case class User(name: String)

  object User:
    given JsonEncoder[User] = DeriveJsonEncoder.gen[User]
    given JsonDecoder[User] = DeriveJsonDecoder.gen[User]

}
