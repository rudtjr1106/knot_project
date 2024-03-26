package com.knot.presentation.ui.main.knotMain.participatingKnot

import com.knot.domain.vo.KnotVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.StateFlow

data class ParticipatingKnotPageState(
    val knotList : StateFlow<List<KnotVo>>
) : PageState