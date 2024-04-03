package com.knot.domain.vo

data class InsideChatRequest(
    val knotId : String = "",
    val id : String = "",
    val isInside : Boolean = false,
)
