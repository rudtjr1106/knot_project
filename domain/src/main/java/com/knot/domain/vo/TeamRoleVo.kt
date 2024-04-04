package com.knot.domain.vo

data class TeamRoleVo(
    val isHost : Boolean = false,
    val leader : String = "",
    val teamUserVo: TeamUserVo = TeamUserVo()
)
