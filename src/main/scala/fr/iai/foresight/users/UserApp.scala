package fr.iai.foresight.users

import zio.*
import zio.http.{Response, *}
import zio.json.*
import fr.iai.foresight.models.*

import java.util.UUID
import scala.util.Try

object UserApp:
  def apply(): Http[UserRepo, Nothing, Request, Response] =
    Http.collectZIO[Request] {
      // POST /user/add
      case req @ (Method.POST -> Root / "user" / "add") =>
        (for {
          userRequest <- req.body.asString.map(_.fromJson[User])
          result <- userRequest match
            case Left(error) => ZIO.debug(s"Failed to parse user: $error").as(Response.text(error).withStatus(Status.BadRequest))
            case Right(user) => UserRepo.register(user).map(id => Response.text(id.toString))
        } yield result).orDie
      // GET /user/$id
      case Method.GET -> Root / "user" / id if Try { UUID.fromString(id) }.isSuccess =>
        (for {
          convertedId <- ZIO.fromTry(Try { UUID.fromString(id) })
          result <- UserRepo.getUser(convertedId)
                      .map(response => Response.json(response.toJson))
        } yield result)
          .orDie
//          .mapError(e => Response.text(s"$e").withStatus(Status.BadRequest))
      // GET /user/all
      case Method.GET -> Root / "user" / "all" =>
        UserRepo.users
          .map(response => Response.json(response.toJson))
          .orDie
    }
