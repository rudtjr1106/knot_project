package com.knot.presentation.ui.main.knotMain.detail.menu.applicants

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.vo.KnotVo
import com.knot.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KnotApplicantsViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase
) : BaseViewModel<KnotApplicantsPageState>() {

    private val knotDetailStateFlow : MutableStateFlow<KnotVo> = MutableStateFlow(KnotVo())

    override val uiState: KnotApplicantsPageState = KnotApplicantsPageState(
        knotDetailStateFlow.asStateFlow(),
    )
    fun getDetail(knotId : String){
        viewModelScope.launch {
            getKnotDetailUseCase(knotId).collect{
                resultResponse(it, ::successGetKnotDetail)
            }
        }
    }

    private fun successGetKnotDetail(result : KnotVo){
        viewModelScope.launch {
            knotDetailStateFlow.update { result }
        }
    }

    fun onClickBack(){
        emitEventFlow(KnotApplicantsEvent.GoToBackEvent)
    }

}