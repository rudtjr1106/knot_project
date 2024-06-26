package com.knot.presentation.ui.main.knotMain.detail

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.CheckKnotTodoUseCase
import com.knot.domain.usecase.knot.DeleteKnotUseCase
import com.knot.domain.usecase.knot.GetChatListUseCase
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.usecase.knot.OutKnotUseCase
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TeamStatisticsDetailVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class KnotDetailViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase,
    private val getChatListUseCase: GetChatListUseCase,
    private val checkKnotTodoUseCase: CheckKnotTodoUseCase,
    private val deleteKnotUseCase: DeleteKnotUseCase,
    private val outKnotUseCase: OutKnotUseCase
) : BaseViewModel<KnotDetailPageState>() {

    companion object{
        const val PERCENT_COUNT = 100

        //HOST MENU
        const val EDIT_RULE_ROLE = 0
        const val EDIT_KNOT = 1
        const val CONFIRM_APPLICANT = 2
        const val DELETE_KNOT = 3

        //GUEST MENU
        const val CHECK_RULE_ROLE = 0
        const val OUT_KNOT = 1
    }

    private val knotDetailStateFlow : MutableStateFlow<KnotVo> = MutableStateFlow(KnotVo())
    private val todoListStateFlow : MutableStateFlow<List<TodoVo>> = MutableStateFlow(emptyList())
    private val myAllStatisticsStateFlow : MutableStateFlow<Int> = MutableStateFlow(0)
    private val lastChatStateFlow : MutableStateFlow<ChatVo> = MutableStateFlow(ChatVo())
    private val otherStatisticsListStateFlow : MutableStateFlow<List<TeamStatisticsDetailVo>> = MutableStateFlow(emptyList())
    private val isHostStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val uiState: KnotDetailPageState = KnotDetailPageState(
        knotDetailStateFlow.asStateFlow(),
        todoListStateFlow.asStateFlow(),
        myAllStatisticsStateFlow.asStateFlow(),
        lastChatStateFlow.asStateFlow(),
        otherStatisticsListStateFlow.asStateFlow(),
        isHostStateFlow.asStateFlow()
    )

    private var chatList : List<ChatVo> = emptyList()
    private var allStatisticsCount = 0

    fun getDetail(knotId : String){
        getKnotDetail(knotId)
        getChatList(knotId)
    }

    private fun getKnotDetail(knotId: String){
        showLoading()
        viewModelScope.launch {
            getKnotDetailUseCase(knotId).collect {
                resultResponse(it, ::successGetKnotDetail)
            }
        }
    }

    private fun getChatList(knotId: String){
        viewModelScope.launch {
            getChatListUseCase(knotId).collect {
                resultResponse(it, ::successGetChatList)
            }
        }
    }

    private fun successGetKnotDetail(result : KnotVo){
        updateTodoList(result.todoList)
        viewModelScope.launch {
            knotDetailStateFlow.update { result }
            isHostStateFlow.update { result.leader == UserInfo.info.id }
            calculateMyStatistics()
            calculateOtherStatistics()
        }
    }

    private fun updateTodoList(todoMap : HashMap<String, TodoVo>){
        val todoList = todoMap.values.toList().filter { it.userId == UserInfo.info.id }.take(5)
        viewModelScope.launch {
            todoListStateFlow.update { todoList }
        }
    }

    private fun successGetChatList(result : List<ChatVo>){
        viewModelScope.launch {
            chatList = result
            lastChatStateFlow.update { result.last() }
            calculateMyStatistics()
            calculateOtherStatistics()
        }
    }

    private fun calculateMyStatistics(){
        if(knotDetailStateFlow.value.knotId.isEmpty() || chatList.isEmpty()){
            return
        }
        updateMyAllStatistics(getAllStatistics(UserInfo.info.id))
    }

    private fun calculateOtherStatistics(){
        if(knotDetailStateFlow.value.knotId.isEmpty() || chatList.isEmpty()){
            return
        }
        val list = mutableListOf<TeamStatisticsDetailVo>()
        knotDetailStateFlow.value.teamList.forEach {
            val vo = TeamStatisticsDetailVo(id = it.value.id, name = it.value.name, statistics = getAllStatistics(it.value.id).toString())
            list.add(vo)
        }
        list.removeIf { it.id == UserInfo.info.id }
        updateOtherStatistics(list)
    }

    private fun getAllStatistics(id : String) : Int {
        allStatisticsCount = 0
        val gatheringStatistics = getGatheringStatistics(id)
        val todoCompleteStatistics = getTodoCompleteStatistics(id)
        val chatStatistics = getChatStatistics(id)
        val allStatistics = ((gatheringStatistics + todoCompleteStatistics + chatStatistics) /
                (allStatisticsCount * PERCENT_COUNT)) * PERCENT_COUNT
        return allStatistics.roundToInt()
    }

    private fun getGatheringStatistics(id : String) : Double {
        val gatheringList = knotDetailStateFlow.value.gatheringList.values.toList()
        if(gatheringList.isEmpty()){
            return 0.0
        }
        allStatisticsCount++
        var participateCount = 0
        gatheringList.forEach { gathering ->
            gathering.participants.values.forEach { user ->
                if(user.id == id) participateCount++
            }
        }

        return (participateCount / gatheringList.size.toDouble()) * PERCENT_COUNT
    }

    private fun getTodoCompleteStatistics(id : String) : Double {
        val todoList = knotDetailStateFlow.value.todoList.values.toList()
        if(todoList.isEmpty()){
            return 0.0
        }
        allStatisticsCount++
        var todoCompleteCount = 0
        todoList.forEach { todo ->
            if((todo.userId == id) && todo.complete) todoCompleteCount++
        }

        return (todoCompleteCount / todoList.size.toDouble()) * PERCENT_COUNT
    }

    private fun getChatStatistics(id : String) : Double {
        allStatisticsCount++
        var chatCount = 0
        chatList.forEach { chat ->
            if(chat.id == id) chatCount++
        }

        return (chatCount / chatList.size.toDouble()) * PERCENT_COUNT
    }

    private fun updateMyAllStatistics(statistics : Int){
        viewModelScope.launch {
            myAllStatisticsStateFlow.update { statistics }
        }
    }

    private fun updateOtherStatistics(list : List<TeamStatisticsDetailVo>){
        viewModelScope.launch {
            otherStatisticsListStateFlow.update { list }
        }
    }

    fun onClickComplete(request: CheckKnotTodoRequest){
        viewModelScope.launch {
            checkKnotTodoUseCase(request).collect{
                resultResponse(it, {getDetail(request.knotId)})
            }
        }
    }

    fun onClickBack(){
        emitEventFlow(KnotDetailEvent.GoToBackEvent)
    }

    fun onClickStatistics(){
        emitEventFlow(KnotDetailEvent.GoToStatisticsEvent)
    }

    fun onClickChat(){
        emitEventFlow(KnotDetailEvent.GoToChatEvent)
    }

    fun onClickTodo(){
        emitEventFlow(KnotDetailEvent.GoToTodoEvent)
    }

    fun onClickMenu(){
        if(isHostStateFlow.value) emitEventFlow(KnotDetailEvent.ShowHostBottomSheet)
        else emitEventFlow(KnotDetailEvent.ShowGuestBottomSheet)
    }

    fun onClickHostBottomSheet(selectItem : String, list : List<String>){
        when(selectItem){
            list[EDIT_RULE_ROLE] -> emitEventFlow(KnotDetailEvent.GoToEditRuleRoleEvent)
            list[EDIT_KNOT] -> emitEventFlow(KnotDetailEvent.GoToEditKnotEvent)
            list[CONFIRM_APPLICANT] -> emitEventFlow(KnotDetailEvent.GoToKnotApplicantsEvent)
            list[DELETE_KNOT] -> deleteKnot()
        }
    }

    fun onClickGuestBottomSheet(selectItem : String, list : List<String>){
        when(selectItem){
            list[CHECK_RULE_ROLE] -> emitEventFlow(KnotDetailEvent.GoToEditRuleRoleEvent)
            list[OUT_KNOT] -> outKnot()
        }
    }

    private fun outKnot(){
        viewModelScope.launch {
            outKnotUseCase(knotDetailStateFlow.value).collect{
                resultResponse(it, {onClickBack() })
            }
        }
    }

    private fun deleteKnot(){
        if(knotDetailStateFlow.value.teamList.size != 1){
            return // TODO 팀원이 전부 나가야 된다는 Dialog
        }
        viewModelScope.launch {
            deleteKnotUseCase(knotDetailStateFlow.value.knotId).collect{
                resultResponse(it, { onClickBack() })
            }
        }
    }
}