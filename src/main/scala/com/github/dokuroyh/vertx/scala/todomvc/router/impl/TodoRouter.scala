package com.github.dokuroyh.vertx.scala.todomvc.router.impl

import scala.util.Failure
import scala.util.Success

import com.github.dokuroyh.vertx.scala.todomvc.model.TodoItem
import com.github.dokuroyh.vertx.scala.todomvc.router.RestRouter
import com.github.dokuroyh.vertx.scala.todomvc.service.impl.InMemoryTodoService

import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.BodyHandler
import scala.concurrent.ExecutionContext

class TodoRouter(override val vertx: Vertx,
                 implicit val executionContext: ExecutionContext)
    extends RestRouter(vertx = vertx) {

  val todoService = new InMemoryTodoService

  router.route().handler(BodyHandler.create.handle)

  // get todo
  router
    .get("/:id")
    .handler(rc => {
      val id = rc.pathParam("id")
      todoService
        .list()
        .map(_.find(_.id == id))
        .onComplete({
          case Success(None)         => rc.response.notFound()
          case Success(Some(result)) => rc.response.ok(result)
          case Failure(err)          => rc.response.error(err)
        })
    })

  // list todos
  router
    .get("/")
    .handler(rc => {
      todoService
        .list()
        .onComplete({
          case Success(result) => rc.response.ok(result)
          case Failure(err)    => rc.response.error(err)
        })
    })

  // create todo
  router
    .post("/")
    .handler(rc => {
      rc.getBody(classOf[TodoItem]) match {
        case None => rc.response.badRequest("Bad Request")
        case Some(todo) =>
          todoService
            .create(todo)
            .onComplete({
              case Success(result) => rc.response.ok(result)
              case Failure(err)    => rc.response.error(err)
            })
      }
    })

  // update todo
  router
    .put("/:id")
    .handler(rc => {
      rc.pathParam("id")
        .flatMap(id => {
          rc.getBody(classOf[TodoItem])
            .map(todo => {
              todo.id = Some(id)
              todo
            })
        }) match {
        case None => rc.response.badRequest("Bad Request")
        case Some(todo) =>
          todoService
            .update(todo.id.get, todo)
            .onComplete({
              case Success(None)         => rc.response.notFound()
              case Success(Some(result)) => rc.response.ok(result)
              case Failure(err)          => rc.response.error(err)
            })
      }
    })

  router
    .delete("/:id")
    .handler(rc => {
      rc.pathParam("id") match {
        case None => rc.response.badRequest("Bad Request")
        case Some(id) =>
          todoService
            .delete(id)
            .onComplete({
              case Success(None)         => rc.response.notFound()
              case Success(Some(result)) => rc.response.noContent()
              case Failure(err)          => rc.response.error(err)
            })
      }
    })
}

object TodoRouter {
  def create(vertx: Vertx, executionContext: ExecutionContext): Router =
    new TodoRouter(vertx, executionContext).router
}
