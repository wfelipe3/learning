package com.io.learing

import org.scalatest.{FreeSpec, Matchers}
import cats.effect.IO

class IOTest extends FreeSpec with Matchers {

  "io monad hello world" in {
    val iomessage = IO {
      "hello world"
    }
    iomessage.unsafeRunSync() should be("hello world")
  }

  "io monad with error" in {
    val io: IO[String] = IO.raiseError(new Exception("error"))
    assertThrows[Exception](io.unsafeRunSync())
  }

  "io testing flatmap" in {
    val io = IO.pure(10)
      .flatMap { n => IO(s"some value $n") }

    io.unsafeRunSync() should be ("some value 10")
  }

}
