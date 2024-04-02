package com.knot.domain.vo

data class ChatVo(
    val content : String = "",
    val date : String = "",
    val id : String = "",
    val name : String = "",
    val readWho : HashMap<String, Boolean> = hashMapOf(),
    val time : String = "",
)