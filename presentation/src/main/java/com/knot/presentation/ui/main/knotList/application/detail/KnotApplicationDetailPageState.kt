package com.knot.presentation.ui.main.knotList.application.detail

import com.knot.domain.vo.KnotVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.StateFlow

data class KnotApplicationDetailPageState(
    val knotDetail : StateFlow<KnotVo>
) : PageState
