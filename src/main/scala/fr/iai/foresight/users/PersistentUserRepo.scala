package fr.iai.foresight.users

import fr.iai.foresight.models
import fr.iai.foresight.models.User
import io.getquill.{Escape, PostgresZioJdbcContext}
import io.getquill.jdbczio.Quill
import io.getquill.*
import zio.*

import java.util.UUID
import javax.sql.DataSource

case class Users(id: UUID, name: String)

case class PersistentUserRepo(ds: DataSource) extends UserRepo:
  val ctx = new PostgresZioJdbcContext(Escape)

  import ctx._

  override def register(user: User): Task[UUID] =
    {for
      id <- Random.nextUUID
      _ <- ctx.run {
        quote {
          query[Users].insertValue {
            lift(Users(id = id, name = user.name))
          }
        }
      }
    yield id}
    .provide(ZLayer.succeed(ds))
    .mapError(
      error => {
        error.printStackTrace()
        error
      }
    )

  override def getUser(id: UUID): Task[Option[User]] =
    ctx.run {
      quote {
        query[Users]
          .filter(_.id == lift(id))
          .map(u => User(u.name))
      }
    }
    .provide(ZLayer.succeed(ds))
    .map(_.headOption)
    .mapError(
      error => {
        error.printStackTrace()
        error
      }
    )

  override def users: Task[List[models.User]] =
    ctx.run {
      quote {
        query[Users].map(u => User(u.name))
      }
    }
    .provide(ZLayer.succeed(ds))
    .mapError(
      error => {
        error.printStackTrace()
        error
      }
    )

object PersistentUserRepo:
  def layer: ZLayer[Any, Throwable, PersistentUserRepo] =
    Quill.DataSource.fromPrefix("UserApp") >>>
      ZLayer.fromFunction(PersistentUserRepo(_))