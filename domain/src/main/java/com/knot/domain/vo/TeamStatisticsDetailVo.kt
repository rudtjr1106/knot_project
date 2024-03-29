package com.knot.domain.vo

import com.knot.domain.enums.StatisticsDetailType

data class TeamStatisticsDetailVo(
    val id : String = "",
    val name : String = "",
    val statistics : String = "",
    val type : StatisticsDetailType = StatisticsDetailType.ALL,
)