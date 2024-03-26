package com.knot.presentation.ui.main.knotMain.calendar

import com.knot.domain.vo.CalendarLayoutVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.StateFlow

data class CalendarPageState(
    val calendarLayoutList : StateFlow<List<CalendarLayoutVo>>
) : PageState