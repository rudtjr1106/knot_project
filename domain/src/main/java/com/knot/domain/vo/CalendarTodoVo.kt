package com.knot.domain.vo

import com.knot.domain.enums.CalendarTodoType


data class CalendarTodoVo(
    val type : CalendarTodoType = CalendarTodoType.TODO,
    val todo : TodoVo = TodoVo(),
    val gathering: GatheringVo = GatheringVo(),
)
