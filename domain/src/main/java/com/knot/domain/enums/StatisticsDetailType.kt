package com.knot.domain.enums

enum class StatisticsDetailType(val type : Int) {
    TODO(0), GATHERING(1), CHAT(2), ALL(3);

    companion object {
        fun valueOf(value: Int): StatisticsDetailType {
            return StatisticsDetailType.values().find {
                it.type == value
            } ?: StatisticsDetailType.TODO
        }
    }
}