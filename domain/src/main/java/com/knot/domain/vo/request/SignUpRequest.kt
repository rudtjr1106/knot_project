package com.knot.domain.vo.request

data class SignUpRequest(
    val email : String = "",
    val id : String = "",
    val intro : String = "",
    val major : String = "",
    val name : String = "",
    val organization : String = ""
)
