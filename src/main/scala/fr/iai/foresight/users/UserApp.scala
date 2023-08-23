package fr.iai.foresight.users

import fr.iai.foresight.models.*
import zio.*
import zio.http.{Response, uuid as uuidParameter, *}
import zio.json.*

import java.util.UUID

//import fr.iai.foresight.security.SecurityHelper.jwtDecode
//import zio.http.HttpAppMiddleware.bearerAuth
//@@ bearerAuth(jwtDecode(_).isDefined)

object UserApp:
  val routes: Routes[UserRepo, Response] = Routes(
    Method.POST / "user" / "add" -> handler { userAdd },
    Method.GET / "user" / uuidParameter("id") -> handler { (id: UUID, req: Request) => userGet(id) },
    Method.GET / "user" / "all" -> handler { userGetAll() },
  )

  private def userAdd(req: Request): ZIO[UserRepo, Response, Response] =
    (for {
      requestBody <- ZIO.succeed(req.body)
      userInRequest <- requestBody.asString.map(_.fromJson[User])
      result <- userInRequest match {
            case Left(error) => ZIO.fail(Response.text(s"Failed to parse user: $error").status(Status.BadRequest))
            case Right(user) => UserRepo.register(user).map(id => Response.text(id.toString))
      }
    } yield result)
      .mapError(error => Response.text(s"Error adding user: $error").status(Status.BadRequest))

  private def userGet(id: UUID): ZIO[UserRepo, Response, Response] =
    (for {
      userFromDB <- UserRepo.getUser(id)
      result <- userFromDB match {
        case None => ZIO.fail(Response.json(s"""{"error" : "No user found for id $id"}"""))
        case Some(user) => ZIO.succeed(Response.json(user.toJson))
      }
    } yield result)
      .mapError(error => Response.text(s"Error getting user: $error").status(Status.BadRequest))

  private def userGetAll(): ZIO[UserRepo, Response, Response] =
    UserRepo.users
      .map(response => Response.json(response.toJson))
      .orDie
