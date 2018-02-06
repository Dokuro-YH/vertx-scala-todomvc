package com.github.dokuroyh.vertx.scala.service

import scala.concurrent.Future

import com.github.dokuroyh.vertx.scala.todomvc.model.TodoItem

trait TodoService {
  def list(): Future[Iterable[TodoItem]]
  def create(todoItem: TodoItem): Future[TodoItem]
  def update(id: String, todoItem: TodoItem): Future[Option[TodoItem]]
  def delete(id: String): Future[Option[TodoItem]]
  def clear(): Future[Iterable[TodoItem]]
}
