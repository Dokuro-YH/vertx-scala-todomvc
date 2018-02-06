package com.github.dokuroyh.vertx.scala.todomvc.service.impl

import java.util.UUID

import scala.Function.const
import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.github.dokuroyh.vertx.scala.service.TodoService
import com.github.dokuroyh.vertx.scala.todomvc.model.TodoItem

class InMemoryTodoService extends TodoService {

  val store: Map[String, TodoItem] = Map()

  override def list(): Future[Iterable[TodoItem]] = Future(store.values)

  override def create(todo: TodoItem): Future[TodoItem] = {
    val id = UUID.randomUUID.toString.replace("-", "")
    todo.id = Some(id)
    store(id) = todo
    Future(todo)
  }

  override def update(id: String, todo: TodoItem): Future[Option[TodoItem]] = {
    todo.id = Some(id)
    val updated = store
      .get(id)
      .map(const(todo))
    if (updated.isDefined) store(id) = updated.get
    Future(updated)
  }

  override def delete(id: String): Future[Option[TodoItem]] = {
    Future(store.remove(id))
  }

  override def clear(): Future[Iterable[TodoItem]] = {
    val todos = store.values
    store.clear()
    Future(todos)
  }
}
