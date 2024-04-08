package com.knot.presentation.ui.main.createOrEditKnot

import com.knot.domain.enums.CreateOrEditKnotType
import com.knot.domain.vo.CategoryVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TeamUserVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CreateOrEditKnotPageState(
    val pageType: StateFlow<CreateOrEditKnotType>,
    val knotDetail: StateFlow<KnotVo>,
    var title : MutableStateFlow<String>,
    var content : MutableStateFlow<String>,
    var userId : MutableStateFlow<String>,
    var isPrivate : MutableStateFlow<Boolean>,
    val teamList : StateFlow<HashMap<String, TeamUserVo>>,
    val categoryList : StateFlow<List<CategoryVo>>
) : PageState