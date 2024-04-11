package com.knot.presentation.ui.main.knotList.application.detail

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.CancelApplicationKnotUseCase
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
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
class KnotApplicationDetailViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase,
    private val cancelApplicationKnotUseCase: CancelApplicationKnotUseCase
) : BaseViewModel<KnotApplicationDetailPageState>() {

    private val knotDetailStateFlow : MutableStateFlow<KnotVo> = MutableStateFlow(KnotVo())
    private val isApplyKnotStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val uiState: KnotApplicationDetailPageState = KnotApplicationDetailPageState(
        knotDetailStateFlow.asStateFlow(),
        isApplyKnotStateFlow.asStateFlow()
    )

    fun getKnotDetail(knotId : String){
        showLoading()
        viewModelScope.launch {
            getKnotDetailUseCase(knotId).collect{
                resultResponse(it, ::successGetKnotDetail)
            }
        }
    }

    private fun successGetKnotDetail(result: KnotVo){
        viewModelScope.launch {
            knotDetailStateFlow.update { result }
            isApplyKnotStateFlow.update { result.applicants.values.any { it.user.id == UserInfo.info.id } }
        }
    }

    fun onClickApply(){
        if(isApplyKnotStateFlow.value) cancelApplication()
        else emitEventFlow(KnotApplicationDetailEvent.GoToApplicationEvent(knotDetailStateFlow.value.knotId, knotDetailStateFlow.value.title))
    }

    private fun cancelApplication(){
        val knotId = knotDetailStateFlow.value.knotId
        showLoading()
        viewModelScope.launch {
            cancelApplicationKnotUseCase(knotId).collect{
                resultResponse(it, {getKnotDetail(knotId)})
            }
        }
    }

    fun onClickBack(){
        emitEventFlow(KnotApplicationDetailEvent.GoToBackEvent)
    }

}