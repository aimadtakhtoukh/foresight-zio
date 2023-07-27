package fr.iai.foresight.users

import fr.iai.foresight.models.User
import zio.{Task, ZIO}

import java.util.UUID

trait UserRepo:
  def register(user: User): Task[UUID]
  def getUser(id: UUID): Task[Option[User]]
  def users: Task[List[User]]

object UserRepo:
  def register(user: User): ZIO[UserRepo, Throwable, UUID] =
    ZIO.serviceWithZIO[UserRepo](_.register(user))
  def getUser(id: UUID): ZIO[UserRepo, Throwable, Option[User]] =
    ZIO.serviceWithZIO[UserRepo](_.getUser(id))
  def users: ZIO[UserRepo, Throwable, List[User]] =
    ZIO.serviceWithZIO[UserRepo](_.users)
