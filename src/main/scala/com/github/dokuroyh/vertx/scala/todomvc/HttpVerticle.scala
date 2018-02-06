package com.github.dokuroyh.vertx.scala.todomvc

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.ext.web.Router

import scala.concurrent.Future
import com.github.dokuroyh.vertx.scala.todomvc.router.impl.TodoRouter
import com.github.dokuroyh.vertx.scala.todomvc.router.impl.TodoRouter
import io.vertx.scala.core.Vertx
import scala.util.Success
import scala.util.Failure

import scala.concurrent.ExecutionContext.Implicits.global

class HttpVerticle extends ScalaVerticle {

  override def startFuture(): Future[_] = {
    val port   = config.getInteger("http.port", 8080)
    val router = Router.router(vertx)

    router
      .get("/")
      .handler(rc => {
        rc.response.end("Hello Vert.x!")
      })

    router.mountSubRouter("/todos", TodoRouter.create(vertx, executionContext))

    vertx
      .createHttpServer()
      .requestHandler(router.accept)
      .listenFuture(port, "0.0.0.0")
  }
}

object Runner {
  def main(args: Array[String]): Unit = {
    val vertx = Vertx.vertx()
    vertx
      .deployVerticleFuture(ScalaVerticle.nameForVerticle[HttpVerticle])
      .onComplete({
        case Success(id) => println(s"Succeeded in deploying verticle: $id")
        case Failure(err) => {
          err.printStackTrace()
          System.exit(1)
        }
      })
  }
}
