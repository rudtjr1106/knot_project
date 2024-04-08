package com.knot.presentation.ui.main.createOrEditKnot

import androidx.lifecycle.viewModelScope
import com.knot.domain.enums.CreateOrEditKnotType
import com.knot.domain.usecase.knot.CreateKnotUseCase
import com.knot.domain.usecase.knot.EditKnotUseCase
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.usecase.sign.GetUserInfoUseCase
import com.knot.domain.vo.CategoryVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TeamUserVo
import com.knot.domain.vo.UserVo
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.KnotLog
import com.knot.presentation.util.UserInfo
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
    private val createKnotUseCase: CreateKnotUseCase,
    private val editKnotUseCase: EditKnotUseCase,
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
    private val categoryListStateFlow : MutableStateFlow<List<CategoryVo>> = MutableStateFlow(
        emptyList()
    )

    private var map : HashMap<String, TeamUserVo> = hashMapOf()


    override val uiState: CreateOrEditKnotPageState = CreateOrEditKnotPageState(
        pageTypeStateFlow.asStateFlow(),
        knotDetailStateFlow.asStateFlow(),
        titleStateFlow,
        contentStateFlow,
        userIdStateFlow,
        isPrivateStateFlow,
        teamListStateFlow.asStateFlow(),
        categoryListStateFlow.asStateFlow()
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
            updateCategoryList(getCategoryList(result.category))
        }
    }

    fun updateEmptyCategoryList(list: List<String>){
        val categoryList = mutableListOf<CategoryVo>()
        list.forEach {
            categoryList.add(CategoryVo(category = it, isSelected = false))
        }
        updateCategoryList(categoryList.sortedByDescending { it.category })
    }

    private fun getCategoryList(categoryVo: HashMap<String, Boolean>) : List<CategoryVo>{
        val categoryList = mutableListOf<CategoryVo>()
        categoryVo.forEach {
            categoryList.add(CategoryVo(category = it.key, isSelected = it.value))
        }
        return categoryList.sortedByDescending { it.category }
    }

    private fun updateCategoryList(categoryList: List<CategoryVo>){
        viewModelScope.launch {
            categoryListStateFlow.update { categoryList }
        }
    }

    fun onClickCategory(categoryVo: CategoryVo){
        val newCategoryList = categoryListStateFlow.value.map {
            if(it.category == categoryVo.category) it.copy(isSelected = !categoryVo.isSelected)
            else it
        }
        updateCategoryList(newCategoryList)
    }

    fun onClickCancelMember(member : TeamUserVo){
        map.remove(member.uid)
        updateTeamUserMap(map)
    }

    fun onClickAddMember(){
        if(userIdStateFlow.value.trim().isEmpty()){
            return
        }
        viewModelScope.launch {
            showLoading()
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
        val newMap = HashMap(map)
        viewModelScope.launch {
            teamListStateFlow.update { newMap }
        }
    }

    fun onClickBack(){
        emitEventFlow(CreateOrEditKnotEvent.GoToBackEvent)
    }

    fun onClickCreateOrSave(){
        when(pageTypeStateFlow.value){
            CreateOrEditKnotType.CREATE -> createKnot()
            CreateOrEditKnotType.EDIT -> editKnot()
        }
    }

    private fun createKnot(){
        val request = getNewKnotRequest()
        viewModelScope.launch {
            showLoading()
            createKnotUseCase(request).collect{
                resultResponse(it, {onClickBack()})
            }
        }
    }

    private fun getNewKnotRequest() : KnotVo {
        return KnotVo(
            category = getKnotCategoryRequest(),
            content = contentStateFlow.value,
            leader = UserInfo.info.id,
            privateKnot = isPrivateStateFlow.value,
            teamList = hashMapOf(UserInfo.info.uid to TeamUserVo(
                id = UserInfo.info.id,
                name = UserInfo.info.id,
                uid = UserInfo.info.uid
            )),
            title = titleStateFlow.value
        )
    }

    private fun editKnot(){
        val request = knotDetailStateFlow.value.copy(
            category = getKnotCategoryRequest(),
            content = contentStateFlow.value,
            privateKnot = isPrivateStateFlow.value,
            title = titleStateFlow.value
        )
        viewModelScope.launch {
            showLoading()
            editKnotUseCase(request).collect{
                resultResponse(it, {onClickBack()})
            }
        }
    }

    private fun getKnotCategoryRequest() : HashMap<String, Boolean>{
        val requestMap = hashMapOf<String, Boolean>()
        categoryListStateFlow.value.forEach {
            requestMap[it.category] = it.isSelected
        }
        return requestMap
    }
}