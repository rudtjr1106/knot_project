package com.knot.presentation.ui.main.knotList.application.detail

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.vo.KnotVo
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.KnotLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KnotApplicationDetailViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase
) : BaseViewModel<KnotApplicationDetailPageState>() {

    private val knotDetailStateFlow : MutableStateFlow<KnotVo> = MutableStateFlow(KnotVo())

    override val uiState: KnotApplicationDetailPageState = KnotApplicationDetailPageState(
        knotDetailStateFlow.asStateFlow()
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
        }
    }

    fun onClickApply(){
        emitEventFlow(KnotApplicationDetailEvent.GoToApplicationEvent(knotDetailStateFlow.value.knotId))
    }

    fun onClickBack(){
        emitEventFlow(KnotApplicationDetailEvent.GoToBackEvent)
    }

}