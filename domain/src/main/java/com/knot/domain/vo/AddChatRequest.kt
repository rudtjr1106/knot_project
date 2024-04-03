package com.knot.domain.vo

data class AddChatRequest(
    val knotId : String = "",
    val time : String = "",
    val chat : ChatVo = ChatVo()
)
