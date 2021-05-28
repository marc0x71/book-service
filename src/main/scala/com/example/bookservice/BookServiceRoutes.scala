package com.example.bookservice

import cats.effect.Sync
import com.example.bookservice.JsonCodecs._
import com.example.bookservice.Model.Book
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl

object BookServiceRoutes {

  def bookStoreRoutes[F[_] : Sync](H: BookStore[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "books" =>
        Ok(H.list)
      case GET -> Root / "book" / title =>
        Ok(H.get(title))
      case request@POST -> Root / "book" =>
        request.decode[Book](book => Ok(H.add(book)))
      case DELETE -> Root / "book" / title =>
        Ok(H.remove(title))
    }
  }
}