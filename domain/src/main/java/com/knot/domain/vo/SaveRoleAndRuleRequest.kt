package com.knot.domain.vo

data class SaveRoleAndRuleRequest(
    val knotId : String = "",
    val teamUserMap: Map<String, TeamUserVo>,
    val rule : String = "",
)
