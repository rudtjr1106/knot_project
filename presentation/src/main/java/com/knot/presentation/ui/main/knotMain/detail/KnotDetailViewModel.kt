package com.knot.presentation.ui.main.knotMain.detail

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.GetChatListUseCase
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.vo.ChatListVo
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TodoVo
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
class KnotDetailViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase,
    private val getChatListUseCase: GetChatListUseCase
) : BaseViewModel<KnotDetailPageState>() {

    companion object{
        const val ALL_PERCENT_COUNT = 300
        const val PERCENT_COUNT = 100
    }

    private val knotDetailStateFlow : MutableStateFlow<KnotVo> = MutableStateFlow(KnotVo())
    private val todoListStateFlow : MutableStateFlow<List<TodoVo>> = MutableStateFlow(emptyList())
    private val myAllStatisticsStateFlow : MutableStateFlow<Int> = MutableStateFlow(0)
    private val lastChatStateFlow : MutableStateFlow<ChatVo> = MutableStateFlow(ChatVo())

    override val uiState: KnotDetailPageState = KnotDetailPageState(
        knotDetailStateFlow.asStateFlow(),
        todoListStateFlow.asStateFlow(),
        myAllStatisticsStateFlow.asStateFlow(),
        lastChatStateFlow.asStateFlow(),
    )

    private var chatList : List<ChatVo> = emptyList()

    fun getDetail(knotId : String){
        getKnotDetail(knotId)
        getChatList(knotId)
    }

    private fun getKnotDetail(knotId: String){
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
            calculateMyStatistics()
        }
    }

    private fun updateTodoList(todoMap : HashMap<String, TodoVo>){
        val todoList = todoMap.values.toList().take(5)
        viewModelScope.launch {
            todoListStateFlow.update { todoList }
        }
    }

    private fun successGetChatList(result : ChatListVo){
        viewModelScope.launch {
            chatList = result.chatList
            lastChatStateFlow.update { result.chatList.last() }
            calculateMyStatistics()
        }
    }

    private fun calculateMyStatistics(){
        if(knotDetailStateFlow.value.knotId.isEmpty() || chatList.isEmpty()){
            return
        }
        val gatheringStatistics = getGatheringStatistics()
        val todoCompleteStatistics = getTodoCompleteStatistics()
        val chatStatistics = getChatStatistics()
        val myAllStatistics = ((gatheringStatistics + todoCompleteStatistics + chatStatistics) /
                ALL_PERCENT_COUNT) * PERCENT_COUNT
        KnotLog.D(gatheringStatistics.toString())
        KnotLog.D(todoCompleteStatistics.toString())
        KnotLog.D(chatStatistics.toString())
        updateMyAllStatistics(myAllStatistics.toInt())
    }

    private fun getGatheringStatistics() : Double {
        val gatheringList = knotDetailStateFlow.value.gatheringList.values.toList()
        var myParticipateCount = 0
        gatheringList.forEach { gathering ->
            gathering.participants.values.forEach { user ->
                if(user.id == UserInfo.info.id) myParticipateCount++
            }
        }

        return (myParticipateCount / gatheringList.size.toDouble()) * PERCENT_COUNT
    }

    private fun getTodoCompleteStatistics() : Double {
        val todoList = knotDetailStateFlow.value.todoList.values.toList()
        var myTodoCompleteCount = 0
        todoList.forEach { todo ->
            if((todo.userId == UserInfo.info.id) && todo.complete) myTodoCompleteCount++
        }

        return (myTodoCompleteCount / todoList.size.toDouble()) * PERCENT_COUNT
    }

    private fun getChatStatistics() : Double {
        var myChatCount = 0
        chatList.forEach { chat ->
            if(chat.id == UserInfo.info.id) myChatCount++
        }

        return (myChatCount / chatList.size.toDouble()) * PERCENT_COUNT
    }

    private fun updateMyAllStatistics(statistics : Int){
        viewModelScope.launch {
            myAllStatisticsStateFlow.update { statistics }
        }
    }
}