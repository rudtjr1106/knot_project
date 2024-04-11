package com.knot.domain.vo

data class UserVo(
    val applyList : HashMap<String, UserApplyVo> = hashMapOf(),
    val email : String = "",
    val id : String = "",
    val intro : String = "",
    val knotList : HashMap<String, KnotVo> = hashMapOf(),
    val major : String = "",
    val name : String = "",
    val organization : String = "",
    val uid : String = ""
)
