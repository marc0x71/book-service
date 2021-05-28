package com.example.bookservice

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    BookServiceServer.stream[IO].compile.drain.as(ExitCode.Success)
}
