package com.knot.domain.enums

enum class CalendarViewType(val type:Int) {
    Calendar(0), TODO(1);

    companion object {
        fun valueOf(value: Int): CalendarViewType {
            return CalendarViewType.values().find {
                it.type == value
            } ?: CalendarViewType.Calendar
        }
    }
}