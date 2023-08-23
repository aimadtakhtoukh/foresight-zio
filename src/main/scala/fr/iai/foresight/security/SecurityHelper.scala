package fr.iai.foresight.security

import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import java.time.Clock

object SecurityHelper {

  implicit val clock: Clock = Clock.systemUTC

  // Secret Authentication key
  val SECRET_KEY = "secretKey"

  // Helper to encode the JWT token
  def jwtEncode(username: String): String = {
    val json = s"""{"user": "${username}"}"""
    val claim = JwtClaim {
      json
    }.issuedNow.expiresIn(300)
    Jwt.encode(claim, SECRET_KEY, JwtAlgorithm.HS512)
  }

  // Helper to decode the JWT token
  def jwtDecode(token: String): Option[JwtClaim] = {
    Jwt.decode(token, SECRET_KEY, Seq(JwtAlgorithm.HS512)).toOption
  }

}
