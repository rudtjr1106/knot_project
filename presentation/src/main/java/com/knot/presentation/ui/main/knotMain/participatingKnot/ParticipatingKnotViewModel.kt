package com.knot.presentation.ui.main.knotMain.participatingKnot

import androidx.lifecycle.viewModelScope
import com.knot.domain.vo.KnotVo
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipatingKnotViewModel @Inject constructor() : BaseViewModel<ParticipatingKnotPageState>() {

    private val knotListStateFlow : MutableStateFlow<List<KnotVo>> = MutableStateFlow(
        emptyList()
    )

    override val uiState: ParticipatingKnotPageState = ParticipatingKnotPageState(
        knotListStateFlow.asStateFlow()
    )

    fun getKnotData() {
        val knotList = mutableListOf<KnotVo>()
        UserInfo.info.knotList.forEach { knotList.add(it.value) }
        viewModelScope.launch {
            knotListStateFlow.update { knotList }
        }
    }

    fun onClickBack(){
        emitEventFlow(ParticipatingKnotEvent.GoToBack)
    }
}