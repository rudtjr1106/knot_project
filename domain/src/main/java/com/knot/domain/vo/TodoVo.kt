package com.knot.domain.vo

data class TodoVo (
    val complete : Boolean = false,
    val content : String = "",
    val endDay : String = "",
    val knotId: String = "",
    val knotTitle : String = "",
    val startDay : String = "",
    val title : String = "",
    val todoId : String = "",
    val userId : String = "",
)
