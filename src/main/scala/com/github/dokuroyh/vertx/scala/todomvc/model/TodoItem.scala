package com.github.dokuroyh.vertx.scala.todomvc.model

case class TodoItem(var id: Option[String],
                    var name: Option[String],
                    var completed: Boolean = false) {}
