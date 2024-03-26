package com.knot.domain.vo

import com.knot.domain.enums.CalendarDayViewType

data class CalendarDayVo(
    val type : CalendarDayViewType = CalendarDayViewType.GONE,
    val isHoliday : Boolean = false,
    val isSaturday : Boolean = false,
    val day : String = "",
    val isTodoExist : Boolean = false,
    val isGatheringExist : Boolean = false,
)
