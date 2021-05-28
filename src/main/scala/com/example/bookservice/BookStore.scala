package com.example.bookservice

import cats.Applicative
import cats.implicits.catsSyntaxApplicativeId
import com.example.bookservice.Model.Book

import scala.collection.mutable

trait BookStore[F[_]] {
  def add(book: Book): F[Book]

  def get(title: String): F[Option[Book]]

  def remove(title: String): F[Option[Book]]

  def list: F[List[Book]]
}

object BookStore {
  implicit def apply[F[_]](implicit ev: BookStore[F]): BookStore[F] = ev

  def impl[F[_] : Applicative]: BookStore[F] = new BookStore[F] {
    val storage: mutable.Map[String, Book] = mutable.HashMap[String, Book]().empty

    override def add(book: Book): F[Book] = {
      storage.put(book.title, book)
      book.pure[F]
    }

    override def get(title: String): F[Option[Book]] = {
      storage.get(title).pure[F]
    }

    override def list: F[List[Book]] = {
      storage.toList.map(_._2).pure[F]
    }

    override def remove(title: String): F[Option[Book]] =
      storage.remove(title).pure[F]
  }
}

/*

  final case class Name(name: String) extends AnyVal
  /**
    * More generally you will want to decouple your edge representations from
    * your internal data structures, however this shows how you can
    * create encoders for your data.
    **/
  final case class Greeting(greeting: String) extends AnyVal
  object Greeting {
    implicit val greetingEncoder: Encoder[Greeting] = new Encoder[Greeting] {
      final def apply(a: Greeting): Json = Json.obj(
        ("message", Json.fromString(a.greeting)),
      )
    }
    implicit def greetingEntityEncoder[F[_]]: EntityEncoder[F, Greeting] =
      jsonEncoderOf[F, Greeting]
  }

  def impl[F[_]: Applicative]: BookStore[F] = new BookStore[F]{
    def hello(n: BookStore.Name): F[BookStore.Greeting] =
        Greeting("Hello, " + n.name).pure[F]
  }
}
*/