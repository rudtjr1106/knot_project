package com.knot.domain.vo

import com.knot.domain.enums.StatisticsDetailType

data class TeamStatisticsVo(
    val type : StatisticsDetailType = StatisticsDetailType.ALL,
    val teamStatisticsList : List<TeamStatisticsDetailVo> = emptyList()
)
