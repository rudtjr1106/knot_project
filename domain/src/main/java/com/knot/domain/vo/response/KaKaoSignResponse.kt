package com.knot.domain.vo.response

data class KaKaoSignResponse(
    val uid : String = "",
    val isNewUser : Boolean = true,
    val token : String = ""
)
