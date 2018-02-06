package com.github.dokuroyh.vertx.scala.todomvc

import org.scalatest.Matchers

import scala.concurrent.Promise

class HttpVerticleSpec extends VerticleTesting[HttpVerticle] with Matchers {

  "HttpVerticle" should "bind to 8080 and answer with 'Hello Vert.x!'" in {
    val promise = Promise[String]

    vertx
      .createHttpClient()
      .getNow(8080, "127.0.0.1", "/", r => {
        r.exceptionHandler(promise.failure)
        r.bodyHandler(b => promise.success(b.toString))
      })

    promise.future.map(res => res should equal("Hello Vert.x!"))
  }

}
