package com.knot.presentation.ui.main.knotList

import com.knot.domain.vo.CategoryVo
import com.knot.domain.vo.KnotVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.StateFlow

data class KnotListPageState(
    val knotList: StateFlow<List<KnotVo>>,
    val categoryList : StateFlow<List<CategoryVo>>
) : PageState