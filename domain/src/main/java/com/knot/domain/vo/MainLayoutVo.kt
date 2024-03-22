package com.knot.domain.vo

import com.knot.domain.enums.MainVIewType

data class MainLayoutVo(
    val type : MainVIewType = MainVIewType.TOP,
    val todoList : List<TodoVo> = emptyList(),
    val participatingKnotList : List<KnotVo> = emptyList(),
    val gatheringList : List<GatheringVo> = emptyList()
)
