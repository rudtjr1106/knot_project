package com.knot.domain.vo

import com.knot.domain.enums.ChatType

data class ChatLayoutVo(
    val type : ChatType = ChatType.DIVIDE,
    val chat : ChatVo = ChatVo()
)