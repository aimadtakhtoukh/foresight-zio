package fr.iai.foresight.security

import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import zio.ZIO
import zio.http.{Handler, HandlerAspect, Header, Headers, Request, Response}

import java.time.Clock
import scala.util.Try

object SecurityHelper:

  implicit val clock: Clock = Clock.systemUTC

  // Secret Authentication key
  val SECRET_KEY = "secretKey"

  def jwtEncode(username: String, key: String): String =
    Jwt.encode(JwtClaim(subject = Some(username)).issuedNow.expiresIn(300), key, JwtAlgorithm.HS512)

  def jwtDecode(token: String, key: String): Try[JwtClaim] =
    Jwt.decode(token, key, Seq(JwtAlgorithm.HS512))

  val bearerAuthWithContext: HandlerAspect[Any, String] =
    HandlerAspect.interceptIncomingHandler(Handler.fromFunctionZIO[Request] { request =>
      request.header(Header.Authorization) match {
        case Some(Header.Authorization.Bearer(token)) =>
          ZIO
            .fromTry(jwtDecode(token.value.asString, SECRET_KEY))
            .orElseFail(Response.badRequest("Invalid or expired token!"))
            .flatMap(claim => ZIO.fromOption(claim.subject).orElseFail(Response.badRequest("Missing subject claim!")))
            .map(u => (request, u))

        case _ => ZIO.fail(Response.unauthorized.addHeaders(Headers(Header.WWWAuthenticate.Bearer(realm = "Access"))))
      }
    })
