package fr.iai.foresight.security

import fr.iai.foresight.security.SecurityHelper._

import java.time.Clock
import zio.*
import zio.http.*
import zio.http.Method.*
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

object SecurityApp /*extends ZIOAppDefault*/ {
  
  //val routes: Routes[Any, Response] = Routes(
  //  Method.GET
  //)

  // Http app that is accessible only via a jwt token
 //def user: HttpApp[Any, Nothing] =
 //  Http.collect[Request] {
 //    case GET -> Root / "user" / name / "greet" =>
 //      Response.text(s"Welcome to the ZIO party! ${name}")
 //  } @@ bearerAuth(jwtDecode(_).isDefined)

 //// App that let's the user login
 //// Login is successful only if the password is the reverse of the username
 //def login: HttpApp[Any, Nothing] =
 //  Http.collect[Request] {
 //    case GET -> Root / "login" / username / password =>
 //      if (password.reverse.hashCode == username.hashCode)
 //        Response.text(jwtEncode(username))
 //      else
 //        Response.text("Invalid username or password.").withStatus(Status.Unauthorized)
 //}

 //// Composing all the HttpApps together
 //val app: HttpApp[Any, Nothing] = login ++ user

 //// Run it like any simple app
 //override val run = Server.serve(app).provide(Server.default)
}
