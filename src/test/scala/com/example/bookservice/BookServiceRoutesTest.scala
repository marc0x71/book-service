package com.example.bookservice

import cats.effect.IO
import com.example.bookservice.JsonCodecs._
import com.example.bookservice.Model._
import munit.CatsEffectSuite
import org.http4s.implicits.{http4sKleisliResponseSyntaxOptionT, http4sLiteralsSyntax}
import org.http4s.{Method, Request, Status}

class BookServiceRoutesTest extends CatsEffectSuite {

  test("Get book returns status code 200 if missing") {
    val store: BookStore[IO] = BookStore.impl[IO]
    store.add(Book("title1", "author1"))
    val request = Request[IO](Method.GET, uri"book" / "missing+title")
    val got = BookServiceRoutes.bookStoreRoutes(store).orNotFound(request)
    assertIO(got.map(_.status), Status.Ok)
    got.flatMap(_.as[String].assertEquals("null"))
  }

  test("Get book returns status code 200 if present") {
    val store: BookStore[IO] = BookStore.impl[IO]
    store.add(Book("title1", "author1"))
    val request = Request[IO](Method.GET, uri"book" / "title1")
    val got = BookServiceRoutes.bookStoreRoutes(store).orNotFound(request)
    assertIO(got.map(_.status), Status.Ok)
    got.flatMap(response => response.as[Book].assertEquals(Book("title1", "author1")))
  }

  test("Get books returns status code 200") {
    val books = List(
      Book("title1", "author1"),
      Book("title2", "author2"),
      Book("title3", "author3")
    )
    val store: BookStore[IO] = BookStore.impl[IO]
    books.foreach(b => store.add(b))
    val request = Request[IO](Method.GET, uri"books")
    val got = BookServiceRoutes.bookStoreRoutes(store).orNotFound(request)
    assertIO(got.map(_.status), Status.Ok)
    got.flatMap(response => response.as[List[Book]].assertEquals(books))
  }

  test("Get books returns status code 200 for empty store") {
    val books = List()
    val store: BookStore[IO] = BookStore.impl[IO]
    val request = Request[IO](Method.GET, uri"books")
    val got = BookServiceRoutes.bookStoreRoutes(store).orNotFound(request)
    assertIO(got.map(_.status), Status.Ok)
    got.flatMap(response => response.as[List[Book]].assertEquals(books))
  }

  test("Add a book returns status code 200") {
    val store: BookStore[IO] = BookStore.impl[IO]
    store.add(Book("title1", "author1"))
    val request = Request[IO](Method.POST, uri"book").withEntity(Book("title2", "author2"))
    val got = BookServiceRoutes.bookStoreRoutes(store).orNotFound(request)
    assertIO(got.map(_.status), Status.Ok)
    got.flatMap(response => response.as[Book].assertEquals(Book("title2", "author2")))
  }

  test("Add a book already returns status code 200 and the author is updated") {
    val store: BookStore[IO] = BookStore.impl[IO]
    store.add(Book("title1", "author1"))
    val request = Request[IO](Method.POST, uri"book").withEntity(Book("title1", "author2"))
    val got = BookServiceRoutes.bookStoreRoutes(store).orNotFound(request)
    assertIO(got.map(_.status), Status.Ok)
    got.flatMap(response => response.as[Book].assertEquals(Book("title1", "author2")))
  }

  test("Remove a book returns status code 200") {
    val books = List(
      Book("title1", "author1"),
      Book("title2", "author2"),
      Book("title3", "author3")
    )
    val store: BookStore[IO] = BookStore.impl[IO]
    books.foreach(b => store.add(b))
    val request = Request[IO](Method.DELETE, uri"book" / "title1")
    val got = BookServiceRoutes.bookStoreRoutes(store).orNotFound(request)
    assertIO(got.map(_.status), Status.Ok)
    got.flatMap(response => response.as[Book].assertEquals(Book("title1", "author1")))
  }

  test("Remove a book that doesn't exists returns status code 200 and the body is 'null'") {
    val books = List(
      Book("title1", "author1"),
      Book("title2", "author2"),
      Book("title3", "author3")
    )
    val store: BookStore[IO] = BookStore.impl[IO]
    books.foreach(b => store.add(b))
    val request = Request[IO](Method.DELETE, uri"book" / "title5")
    val got = BookServiceRoutes.bookStoreRoutes(store).orNotFound(request)
    assertIO(got.map(_.status), Status.Ok)
    got.flatMap(_.as[String].assertEquals("null"))
  }

}
