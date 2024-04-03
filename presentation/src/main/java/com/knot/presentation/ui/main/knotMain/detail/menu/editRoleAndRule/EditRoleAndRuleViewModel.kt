package com.knot.presentation.ui.main.knotMain.detail.menu.editRoleAndRule

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TeamRoleVo
import com.knot.domain.vo.TeamStatisticsDetailVo
import com.knot.domain.vo.TeamUserVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.ui.main.knotMain.detail.KnotDetailPageState
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRoleAndRuleViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase
) : BaseViewModel<EditRoleAndRulePageState>() {

    private val knotDetailStateFlow : MutableStateFlow<KnotVo> = MutableStateFlow(KnotVo())
    private val isHostStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val ruleTextStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val teamRoleListStateFlow : MutableStateFlow<List<TeamRoleVo>> = MutableStateFlow(
        emptyList()
    )

    override val uiState: EditRoleAndRulePageState = EditRoleAndRulePageState(
        knotDetailStateFlow.asStateFlow(),
        isHostStateFlow.asStateFlow(),
        ruleTextStateFlow,
        teamRoleListStateFlow.asStateFlow()
    )

    fun getKnotDetail(knotId: String){
        showLoading()
        viewModelScope.launch {
            getKnotDetailUseCase(knotId).collect {
                resultResponse(it, ::successGetKnotDetail)
            }
        }
    }

    private fun successGetKnotDetail(result : KnotVo){
        updateTeamRole(result.teamList.values.toList())
        viewModelScope.launch {
            knotDetailStateFlow.update { result }
            isHostStateFlow.update { result.leader == UserInfo.info.id }
            ruleTextStateFlow.update { result.rule }
        }
    }

    private fun updateTeamRole(list: List<TeamUserVo>){
        val teamRoleList = list.map { TeamRoleVo(isHost = (it.id == UserInfo.info.id), teamUserVo = it) }
        viewModelScope.launch {
            teamRoleListStateFlow.update { teamRoleList.sortedByDescending { it.isHost } }
        }
    }

    fun onClickBack(){
        emitEventFlow(EditRoleAndRuleEvent.GoToBack)
    }

    fun onClickEditRole(){
        emitEventFlow(EditRoleAndRuleEvent.OnClickEditRule)
    }

}