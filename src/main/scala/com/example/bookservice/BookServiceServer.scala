package com.example.bookservice

import cats.effect.{ConcurrentEffect, Timer}
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object BookServiceServer {

  def stream[F[_] : ConcurrentEffect](implicit T: Timer[F]): Stream[F, Nothing] = {
    val helloWorldAlg = BookStore.impl[F]

    val httpApp = BookServiceRoutes.bookStoreRoutes[F](helloWorldAlg).orNotFound

    val finalHttpApp = Logger.httpApp(logHeaders = false, logBody = false)(httpApp)

    BlazeServerBuilder[F](global)
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(finalHttpApp)
      .serve
  }.drain
}
