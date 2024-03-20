package com.knot.domain.base

data class Response<Vo>(
    val data : Vo,
    val result : Int
)
