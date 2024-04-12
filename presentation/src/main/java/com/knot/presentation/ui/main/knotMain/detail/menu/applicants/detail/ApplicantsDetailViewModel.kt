package com.knot.presentation.ui.main.knotMain.detail.menu.applicants.detail

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.ApproveKnotApplicantUseCase
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.usecase.knot.RejectKnotApplicantUseCase
import com.knot.domain.vo.ApplicantUserVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.RejectOrApproveTeamRequest
import com.knot.domain.vo.TeamUserVo
import com.knot.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicantsDetailViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase,
    private val approveKnotApplicantUseCase: ApproveKnotApplicantUseCase,
    private val rejectKnotApplicantUseCase: RejectKnotApplicantUseCase
) : BaseViewModel<ApplicantsDetailPageState>() {

    private val applicantUserStateFlow : MutableStateFlow<ApplicantUserVo> = MutableStateFlow(ApplicantUserVo())

    override val uiState: ApplicantsDetailPageState = ApplicantsDetailPageState(
        applicantUserStateFlow.asStateFlow(),
    )

    private lateinit var knot: KnotVo

    fun getData(knotId : String, userId : String){
        viewModelScope.launch {
            showLoading()
            getKnotDetailUseCase(knotId).collect{ response ->
                resultResponse(response, { successGetKnotDetail(it, userId)})
            }
        }
    }

    private fun successGetKnotDetail(result : KnotVo, userId : String){
        knot = result
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

    fun onClickReject(){
        val request = getRejectOrApproveRequest()
        viewModelScope.launch {
            rejectKnotApplicantUseCase(request).collect{
                resultResponse(it, { onClickBack() })
            }
        }
    }

    fun onClickApprove(){
        val request = getRejectOrApproveRequest()
        viewModelScope.launch {
            approveKnotApplicantUseCase(request).collect{
                resultResponse(it, { onClickBack() })
            }
        }
    }

    private fun getRejectOrApproveRequest() : RejectOrApproveTeamRequest{
        return RejectOrApproveTeamRequest(
            knot = knot,
            teamUserVo = TeamUserVo(
                id = applicantUserStateFlow.value.user.id,
                name = applicantUserStateFlow.value.user.name,
                uid = applicantUserStateFlow.value.user.uid
            )
        )
    }
}