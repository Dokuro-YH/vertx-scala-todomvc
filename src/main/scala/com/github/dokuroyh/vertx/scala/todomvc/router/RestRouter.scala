package com.github.dokuroyh.vertx.scala.todomvc.router

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import io.vertx.scala.core.Vertx
import io.vertx.scala.core.http.HttpServerResponse
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.RoutingContext

abstract class RestRouter(val vertx: Vertx) {
  lazy val router = Router.router(vertx)

  private val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  implicit class RoutingContentExtension(rc: RoutingContext) {

    def getBody[T](clazz: Class[T]): Option[T] = {
      rc.getBodyAsString()
        .map(mapper.readValue(_, clazz))
    }

  }

  implicit class HttpServerResponseExtension(response: HttpServerResponse) {

    def ok(payload: Object): Unit = {
      response
        .putHeader("content-type", "application/json")
        .end(mapper.writeValueAsString(payload))
    }

    def created(): Unit = {
      response
        .setStatusCode(201)
        .end()
    }

    def noContent(): Unit = {
      response
        .setStatusCode(204)
        .end()
    }

    def badRequest(msg: String): Unit = {
      response
        .setStatusCode(401)
        .end()
    }

    def notFound(): Unit = {
      response
        .setStatusCode(404)
        .end()
    }

    def error(err: Throwable): Unit = {
      response
        .setStatusCode(500)
        .end()
    }
  }
}
