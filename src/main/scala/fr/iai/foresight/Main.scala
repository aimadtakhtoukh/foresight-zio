package fr.iai.foresight

import fr.iai.foresight.security.SecurityApp.{login, user}
import fr.iai.foresight.users.{PersistentUserRepo, UserApp}
import zio.*
import zio.http.*

object Main extends ZIOAppDefault :
  override def run: ZIO[Environment with ZIOAppArgs with Scope, Throwable, ExitCode] =
    Server
      .serve(
        UserApp.routes ++ login ++ user
      )
      .provide(
        Server.defaultWithPort(8080),
        PersistentUserRepo.layer
      )
