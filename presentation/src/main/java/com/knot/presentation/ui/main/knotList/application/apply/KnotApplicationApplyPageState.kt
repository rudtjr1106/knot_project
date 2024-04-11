package com.knot.presentation.ui.main.knotList.application.apply

import com.knot.presentation.PageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class KnotApplicationApplyPageState(
    val knotId : StateFlow<String>,
    val knotTitle : StateFlow<String>,
    var applyContent : MutableStateFlow<String>
) : PageState
