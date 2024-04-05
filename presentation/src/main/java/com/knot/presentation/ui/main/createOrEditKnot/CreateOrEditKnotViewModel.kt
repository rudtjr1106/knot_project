package com.knot.presentation.ui.main.createOrEditKnot

import androidx.lifecycle.viewModelScope
import com.knot.domain.enums.CreateOrEditKnotType
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.usecase.sign.GetUserInfoUseCase
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TeamUserVo
import com.knot.domain.vo.UserVo
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.KnotLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateOrEditKnotViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : BaseViewModel<CreateOrEditKnotPageState>() {

    private val pageTypeStateFlow : MutableStateFlow<CreateOrEditKnotType> = MutableStateFlow(CreateOrEditKnotType.CREATE)
    private val knotDetailStateFlow : MutableStateFlow<KnotVo> = MutableStateFlow(KnotVo())
    private val titleStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val contentStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val userIdStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val isPrivateStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val teamListStateFlow : MutableStateFlow<HashMap<String, TeamUserVo>> = MutableStateFlow(
        hashMapOf()
    )


    override val uiState: CreateOrEditKnotPageState = CreateOrEditKnotPageState(
        pageTypeStateFlow.asStateFlow(),
        knotDetailStateFlow.asStateFlow(),
        titleStateFlow,
        contentStateFlow,
        userIdStateFlow,
        isPrivateStateFlow.asStateFlow(),
        teamListStateFlow.asStateFlow()
    )

    fun getData(type : CreateOrEditKnotType, knotId : String){
        viewModelScope.launch {
            pageTypeStateFlow.update { type }
        }
        when(type){
            CreateOrEditKnotType.CREATE -> {}
            CreateOrEditKnotType.EDIT -> getKnotDetail(knotId)
        }
    }

    private fun getKnotDetail(knotId: String){
        viewModelScope.launch {
            getKnotDetailUseCase(knotId).collect{
                resultResponse(it, ::successGetKnotDetail)
            }
        }
    }

    private fun successGetKnotDetail(result : KnotVo){
        viewModelScope.launch {
            knotDetailStateFlow.update { result }
            titleStateFlow.update { result.title }
            contentStateFlow.update { result.content }
            isPrivateStateFlow.update { result.privateKnot }
        }
    }

    fun onClickCancelMember(member : TeamUserVo){
        teamListStateFlow.value.remove(member.id)
        updateTeamUserMap(teamListStateFlow.value)
    }

    fun onClickAddMember(){
        if(userIdStateFlow.value.trim().isEmpty()){
            return
        }
        viewModelScope.launch {
            getUserInfoUseCase(userIdStateFlow.value).collect{
                resultResponse(it, ::successGetUserInfo, ::errorGetUserInfo)
            }
        }
    }

    private fun successGetUserInfo(result : UserVo){
        updateTeamUserMap(getNewTeamUserMap(result))
        updateUserIdEmpty()
    }

    private fun getNewTeamUserMap(userVo: UserVo) : HashMap<String, TeamUserVo>{
        val map = teamListStateFlow.value
        map[userVo.uid] = TeamUserVo(
            id = userVo.id,
            name = userVo.name,
            role = "",
            uid = userVo.uid
        )
        return map
    }

    private fun errorGetUserInfo(error : Int){
        KnotLog.D("존재하지 않은 이용자")
        updateUserIdEmpty()
    }

    private fun updateUserIdEmpty(){
        viewModelScope.launch {
            userIdStateFlow.update { "" }
        }
    }

    private fun updateTeamUserMap(map : HashMap<String, TeamUserVo>){
        viewModelScope.launch {
            teamListStateFlow.update { map }
        }
    }

    fun onClickBack(){
        emitEventFlow(CreateOrEditKnotEvent.GoToBackEvent)
    }
}