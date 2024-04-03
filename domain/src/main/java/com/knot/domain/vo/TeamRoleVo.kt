package com.knot.domain.vo

data class TeamRoleVo(
    val isHost : Boolean = false,
    val teamUserVo: TeamUserVo = TeamUserVo()
)
