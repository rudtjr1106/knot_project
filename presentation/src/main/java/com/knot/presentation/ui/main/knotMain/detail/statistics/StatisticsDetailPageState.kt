package com.knot.presentation.ui.main.knotMain.detail.statistics

import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TeamStatisticsVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.StateFlow

data class StatisticsDetailPageState(
    val knotDetail: StateFlow<KnotVo>,
    val myAllStatistics : StateFlow<Int>,
    val teamStatisticsList : StateFlow<List<TeamStatisticsVo>>
) : PageState