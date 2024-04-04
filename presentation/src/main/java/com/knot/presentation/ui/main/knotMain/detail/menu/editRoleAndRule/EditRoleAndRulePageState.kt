package com.knot.presentation.ui.main.knotMain.detail.menu.editRoleAndRule

import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TeamRoleVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class EditRoleAndRulePageState(
    val knotDetail: StateFlow<KnotVo>,
    val isHost : StateFlow<Boolean>,
    var ruleText : MutableStateFlow<String>,
    val teamRoleList : StateFlow<List<TeamRoleVo>>
) : PageState