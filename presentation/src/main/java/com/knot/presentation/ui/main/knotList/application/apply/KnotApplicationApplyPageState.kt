package com.knot.presentation.ui.main.knotList.application.apply

import com.knot.presentation.PageState
import kotlinx.coroutines.flow.MutableStateFlow

data class KnotApplicationApplyPageState(
    var applyContent : MutableStateFlow<String>
) : PageState
