package com.knot.presentation.ui.main.knotMain.detail

import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TeamStatisticsDetailVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.StateFlow

data class KnotDetailPageState(
    val knotDetail: StateFlow<KnotVo>,
    val todoList : StateFlow<List<TodoVo>>,
    val myAllStatistics : StateFlow<Int>,
    val lastChat: StateFlow<ChatVo>,
    val otherStatisticsList : StateFlow<List<TeamStatisticsDetailVo>>
) : PageState