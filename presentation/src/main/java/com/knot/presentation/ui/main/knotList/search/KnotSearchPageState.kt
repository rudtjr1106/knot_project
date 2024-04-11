package com.knot.presentation.ui.main.knotList.search

import com.knot.domain.vo.KnotVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class KnotSearchPageState(
    val knotList: StateFlow<List<KnotVo>>,
    var searchContent : MutableStateFlow<String>,
    val isEmptyList : StateFlow<Boolean>
) : PageState