package com.knot.domain.vo

data class RejectOrApproveTeamRequest(
    val knot : KnotVo = KnotVo(),
    val teamUserVo: TeamUserVo = TeamUserVo()
)
