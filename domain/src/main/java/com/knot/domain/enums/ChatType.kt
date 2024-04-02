package com.knot.domain.enums

enum class ChatType(val type:Int) {
    DIVIDE(0), MY_CHAT(1), OTHER_CHAT(2), OTHER_SAME_CHAT(3);

    companion object {
        fun valueOf(value: Int): ChatType {
            return ChatType.values().find {
                it.type == value
            } ?: ChatType.DIVIDE
        }
    }
}