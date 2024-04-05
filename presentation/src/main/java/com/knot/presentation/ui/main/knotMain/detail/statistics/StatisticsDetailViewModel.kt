package com.knot.presentation.ui.main.knotMain.detail.statistics

import androidx.lifecycle.viewModelScope
import com.knot.domain.enums.StatisticsDetailType
import com.knot.domain.usecase.knot.GetChatListUseCase
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TeamStatisticsDetailVo
import com.knot.domain.vo.TeamStatisticsVo
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.KnotLog
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class StatisticsDetailViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase,
    private val getChatListUseCase: GetChatListUseCase
) : BaseViewModel<StatisticsDetailPageState>() {

    companion object{
        const val PERCENT_COUNT = 100
    }

    private val knotDetailStateFlow : MutableStateFlow<KnotVo> = MutableStateFlow(KnotVo())
    private val myAllStatisticsStateFlow : MutableStateFlow<Int> = MutableStateFlow(0)
    private val teamStatisticsListStateFlow : MutableStateFlow<List<TeamStatisticsVo>> = MutableStateFlow(
        emptyList()
    )

    override val uiState: StatisticsDetailPageState = StatisticsDetailPageState(
        knotDetailStateFlow.asStateFlow(),
        myAllStatisticsStateFlow.asStateFlow(),
        teamStatisticsListStateFlow.asStateFlow(),
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
        viewModelScope.launch {
            knotDetailStateFlow.update { result }
            calculateMyStatistics()
            calculateOtherStatistics()
        }
    }

    private fun successGetChatList(result : List<ChatVo>){
        viewModelScope.launch {
            chatList = result
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
        val allStatisticsList = getAllTypeTeamStatistics()
        val todoStatisticsList = getTodoTypeTeamStatistics()
        val chatStatisticsList = getChatTypeTeamStatistics()
        val gatheringStatisticsList = getGatheringTypeTeamStatistics()
        updateTeamStatistics(allStatisticsList + todoStatisticsList + chatStatisticsList + gatheringStatisticsList)
    }

    private fun getAllTypeTeamStatistics() : List<TeamStatisticsVo>{
        val list = mutableListOf<TeamStatisticsDetailVo>()
        knotDetailStateFlow.value.teamList.forEach {
            val vo = TeamStatisticsDetailVo(id = it.value.id, name = it.value.name, statistics = getAllStatistics(it.value.id).toString(), type = StatisticsDetailType.ALL)
            list.add(vo)
        }
        val sortedList = list.sortedByDescending { it.statistics.toDouble() }
        return listOf(TeamStatisticsVo(type = StatisticsDetailType.ALL, teamStatisticsList = sortedList))
    }

    private fun getTodoTypeTeamStatistics() : List<TeamStatisticsVo>{
        val list = mutableListOf<TeamStatisticsDetailVo>()
        knotDetailStateFlow.value.teamList.forEach {
            val vo = TeamStatisticsDetailVo(id = it.value.id, name = it.value.name, statistics = getTodoCompleteStatistics(it.value.id).toString(), type = StatisticsDetailType.TODO)
            list.add(vo)
        }
        val sortedList = list.sortedByDescending { it.statistics.toDouble() }
        return listOf(TeamStatisticsVo(type = StatisticsDetailType.TODO, teamStatisticsList = sortedList))
    }

    private fun getChatTypeTeamStatistics() : List<TeamStatisticsVo>{
        val list = mutableListOf<TeamStatisticsDetailVo>()
        knotDetailStateFlow.value.teamList.forEach {
            val vo = TeamStatisticsDetailVo(id = it.value.id, name = it.value.name, statistics = getChatStatistics(it.value.id).toString(), type = StatisticsDetailType.CHAT)
            list.add(vo)
        }
        val sortedList = list.sortedByDescending { it.statistics.toDouble() }
        return listOf(TeamStatisticsVo(type = StatisticsDetailType.CHAT, teamStatisticsList = sortedList))
    }

    private fun getGatheringTypeTeamStatistics() : List<TeamStatisticsVo>{
        val list = mutableListOf<TeamStatisticsDetailVo>()
        knotDetailStateFlow.value.teamList.forEach {
            val vo = TeamStatisticsDetailVo(id = it.value.id, name = it.value.name, statistics = getGatheringStatistics(it.value.id).toString(), type = StatisticsDetailType.GATHERING)
            list.add(vo)
        }
        val sortedList = list.sortedByDescending { it.statistics.toDouble() }
        return listOf(TeamStatisticsVo(type = StatisticsDetailType.GATHERING, teamStatisticsList = sortedList))
    }

    private fun getAllStatistics(id: String): Int {
        allStatisticsCount = 0
        val gatheringStatistics = getGatheringStatistics(id)
        val todoCompleteStatistics = getTodoCompleteStatistics(id)
        val chatStatistics = getChatStatistics(id)
        return (((gatheringStatistics + todoCompleteStatistics + chatStatistics) /
                (allStatisticsCount * PERCENT_COUNT).toDouble()) * PERCENT_COUNT).roundToInt()
    }

    private fun getGatheringStatistics(id : String) : Int {
        val gatheringList = knotDetailStateFlow.value.gatheringList.values.toList()
        if(gatheringList.isEmpty()){
            return 0
        }
        allStatisticsCount++
        var participateCount = 0
        gatheringList.forEach { gathering ->
            gathering.participants.values.forEach { user ->
                if(user.id == id) participateCount++
            }
        }

        return ((participateCount / gatheringList.size.toDouble()) * PERCENT_COUNT).roundToInt()
    }

    private fun getTodoCompleteStatistics(id : String) : Int {
        val todoList = knotDetailStateFlow.value.todoList.values.toList()
        if(todoList.isEmpty()){
            return 0
        }
        allStatisticsCount++
        var todoCompleteCount = 0
        todoList.forEach { todo ->
            if((todo.userId == id) && todo.complete) todoCompleteCount++
        }

        return ((todoCompleteCount / todoList.size.toDouble()) * PERCENT_COUNT).roundToInt()
    }

    private fun getChatStatistics(id : String) : Int {
        var chatCount = 0
        allStatisticsCount++
        chatList.forEach { chat ->
            if(chat.id == id) chatCount++
        }

        return ((chatCount / chatList.size.toDouble()) * PERCENT_COUNT).roundToInt()
    }

    private fun updateMyAllStatistics(statistics : Int){
        viewModelScope.launch {
            myAllStatisticsStateFlow.update { statistics }
        }
    }

    private fun updateTeamStatistics(list : List<TeamStatisticsVo>){
        viewModelScope.launch {
            teamStatisticsListStateFlow.update { list }
        }
    }

    fun onClickBack(){
        emitEventFlow(StatisticsDetailEvent.GoToBackEvent)
    }

    fun getBestTeamName() : String{
        return teamStatisticsListStateFlow.value.first().teamStatisticsList.first().name
    }

    fun getBestTeamAllStatistics() : String{
        return teamStatisticsListStateFlow.value.first().teamStatisticsList.first().statistics
    }

    fun getBestTeamTodoStatistics() : Int{
        val todoList = knotDetailStateFlow.value.todoList.values.toList()
        var todoCompleteCount = 0
        todoList.forEach { todo ->
            if((todo.userId == teamStatisticsListStateFlow.value.first().teamStatisticsList.first().id) && todo.complete) todoCompleteCount++
        }
        return todoCompleteCount
    }

    fun getBestTeamChatStatistics() : Int{
        return getChatStatistics(teamStatisticsListStateFlow.value.first().teamStatisticsList.first().id)
    }

    fun getBestTeamGatheringStatistics() : Int{
        val gatheringList = knotDetailStateFlow.value.gatheringList.values.toList()
        var participateCount = 0
        gatheringList.forEach { gathering ->
            gathering.participants.values.forEach { user ->
                if(user.id == teamStatisticsListStateFlow.value.first().teamStatisticsList.first().id) participateCount++
            }
        }
        return participateCount
    }
}