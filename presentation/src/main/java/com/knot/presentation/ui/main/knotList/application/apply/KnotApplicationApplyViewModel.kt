package com.knot.presentation.ui.main.knotList.application.apply

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.ApplyKnotUseCase
import com.knot.domain.vo.ApplyKnotRequest
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KnotApplicationApplyViewModel @Inject constructor(
    private val applyKnotUseCase: ApplyKnotUseCase,
) : BaseViewModel<KnotApplicationApplyPageState>() {

    private val knotIdStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val knotTitleStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val applyContentStateFlow : MutableStateFlow<String> = MutableStateFlow("")

    override val uiState: KnotApplicationApplyPageState = KnotApplicationApplyPageState(
        knotIdStateFlow.asStateFlow(),
        knotTitleStateFlow.asStateFlow(),
        applyContentStateFlow
    )

    fun setKnot(id : String, title : String){
        viewModelScope.launch {
            knotIdStateFlow.update { id }
            knotTitleStateFlow.update { title }
        }
    }

    fun onClickApply(){
        val request = ApplyKnotRequest(
            knotId = knotIdStateFlow.value,
            knotTitle = knotTitleStateFlow.value,
            moreIntro = applyContentStateFlow.value,
            userVo = UserInfo.info
        )

        viewModelScope.launch {
            showLoading()
            applyKnotUseCase(request).collect{
                resultResponse(it, { onClickBack() })
            }
        }
    }

    fun onClickBack(){
        emitEventFlow(KnotApplicationApplyEvent.GoToBackEvent)
    }

}