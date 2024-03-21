package com.knot.domain.vo.normal

import com.knot.domain.enums.MainVIewType

data class MainLayoutVo(
    val type : MainVIewType = MainVIewType.TOP,
    val todoList : List<String> = emptyList(),
    val participatingKnotList : List<String> = emptyList()
)
