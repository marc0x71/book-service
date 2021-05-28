package com.example.bookservice

import cats.effect.Sync
import com.example.bookservice.Model.Book
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

object JsonCodecs {
  implicit val bookDecoder: Decoder[Book] = deriveDecoder[Book]

  implicit def bookEntityDecoder[F[_] : Sync]: EntityDecoder[F, Book] =
    jsonOf

  implicit def booksEntityDecoder[F[_] : Sync]: EntityDecoder[F, List[Book]] =
    jsonOf

  implicit val bookEncoder: Encoder[Book] = deriveEncoder[Book]

  implicit def bookEntityEncoder[F[_]]: EntityEncoder[F, Book] =
    jsonEncoderOf

}
