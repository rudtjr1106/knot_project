package com.knot.presentation.ui.main.knotMain

import com.knot.domain.vo.normal.MainLayoutVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.StateFlow

data class MainPageState(
    val mainLayoutList : StateFlow<List<MainLayoutVo>>
) : PageState