package com.knot.presentation.ui.main.knotMain.detail.menu.applicants.detail

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.vo.ApplicantUserVo
import com.knot.domain.vo.KnotVo
import com.knot.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicantsDetailViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase
) : BaseViewModel<ApplicantsDetailPageState>() {

    private val applicantUserStateFlow : MutableStateFlow<ApplicantUserVo> = MutableStateFlow(ApplicantUserVo())

    override val uiState: ApplicantsDetailPageState = ApplicantsDetailPageState(
        applicantUserStateFlow.asStateFlow(),
    )

    fun getData(knotId : String, userId : String){
        viewModelScope.launch {
            showLoading()
            getKnotDetailUseCase(knotId).collect{ response ->
                resultResponse(response, { successGetKnotDetail(it, userId)})
            }
        }
    }

    private fun successGetKnotDetail(result : KnotVo, userId : String){
        val applicants = result.applicants[userId]
        if(applicants != null){
            viewModelScope.launch {
                applicantUserStateFlow.update { applicants }
            }
        }
    }

    fun onClickBack(){
        emitEventFlow(ApplicantsDetailEvent.GoToBackEvent)
    }
}