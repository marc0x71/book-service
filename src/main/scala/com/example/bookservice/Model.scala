package com.example.bookservice

import cats.effect.Sync
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

object Model {
  case class Book(title: String, author: String)

  implicit val bookDecoder: Decoder[Book] = deriveDecoder[Book]

  implicit def bookEntityDecoder[F[_] : Sync]: EntityDecoder[F, Book] =
    jsonOf

  implicit val bookEncoder: Encoder[Book] = deriveEncoder[Book]

  /*
    implicit def bookEntityEncoder[F[_] : Applicative]: EntityEncoder[F, Book] =
      jsonEncoderOf
  */
}
