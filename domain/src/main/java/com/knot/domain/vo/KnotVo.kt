package com.knot.domain.vo

data class KnotVo (
    val content : String = "",
    val gatheringList : HashMap<String, GatheringVo> = hashMapOf(),
    val knotId : String = "",
    val leader : String = "",
    val teamList : HashMap<String, TeamUserVo> = hashMapOf(),
    val title : String = "",
    val todoList : HashMap<String, TodoVo> = hashMapOf()
)
