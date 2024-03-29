package com.knot.domain.vo

data class CheckKnotTodoRequest(
    val knotId : String = "",
    val todoId : String = "",
    val complete : Boolean = false,
)