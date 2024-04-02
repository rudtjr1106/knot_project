package com.knot.domain.vo

data class GatheringVo (
    val content : String = "",
    val gatheringDate : String = "",
    val gatheringId : String = "",
    val participants : HashMap<String, TeamUserVo> = hashMapOf(),
    val title : String = ""
)