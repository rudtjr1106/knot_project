package com.knot.domain.vo

import com.knot.domain.enums.CalendarViewType

data class CalendarLayoutVo(
    val type : CalendarViewType = CalendarViewType.Calendar,
    val knotVo: KnotVo = KnotVo(),
)