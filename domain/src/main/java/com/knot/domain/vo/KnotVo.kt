package com.knot.domain.vo

data class KnotVo (
    val category : HashMap<String, Boolean> = hashMapOf(),
    val content : String = "",
    val gatheringList : HashMap<String, GatheringVo> = hashMapOf(),
    val knotId : String = "",
    val leader : String = "",
    val privateKnot : Boolean = true,
    val rule : String = "",
    val teamList : HashMap<String, TeamUserVo> = hashMapOf(),
    val title : String = "",
    val todoList : HashMap<String, TodoVo> = hashMapOf()
)
