package com.knot.domain.vo

data class CalendarDayVo(
    val isHoliday : Boolean = false,
    val isSaturday : Boolean = false,
    val day : String = "",
    val isTodoExist : Boolean = false,
    val isGatheringExist : Boolean = false,
)
