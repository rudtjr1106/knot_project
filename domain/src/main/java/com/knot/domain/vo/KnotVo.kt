package com.knot.domain.vo

data class KnotVo (
    val applicants : HashMap<String, ApplicantUserVo> = hashMapOf(),
    val category : HashMap<String, Boolean> = hashMapOf(),
    val content : String = "",
    val createdAt : String = "",
    val gatheringList : HashMap<String, GatheringVo> = hashMapOf(),
    val knotId : String = "",
    val leader : String = "",
    val privateKnot : Boolean = true,
    val rule : String = "",
    val teamList : HashMap<String, TeamUserVo> = hashMapOf(),
    val title : String = "",
    val todoList : HashMap<String, TodoVo> = hashMapOf()
)
