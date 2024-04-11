package com.knot.presentation.ui.main.knotList.application.apply

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.ApplyKnotUseCase
import com.knot.domain.vo.ApplyKnotRequest
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KnotApplicationApplyViewModel @Inject constructor(
    private val applyKnotUseCase: ApplyKnotUseCase,
) : BaseViewModel<KnotApplicationApplyPageState>() {

    private val applyContentStateFlow : MutableStateFlow<String> = MutableStateFlow("")

    override val uiState: KnotApplicationApplyPageState = KnotApplicationApplyPageState(
        applyContentStateFlow
    )

    private lateinit var knotId : String

    fun setKnotId(id : String){
        knotId = id
    }

    fun onClickApply(){
        val request = ApplyKnotRequest(
            knotId = knotId,
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