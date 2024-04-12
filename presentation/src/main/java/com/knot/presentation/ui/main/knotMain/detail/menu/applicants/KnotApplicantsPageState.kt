package com.knot.presentation.ui.main.knotMain.detail.menu.applicants

import com.knot.domain.vo.KnotVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.StateFlow

data class KnotApplicantsPageState(
    val knotDetail: StateFlow<KnotVo>,
) : PageState