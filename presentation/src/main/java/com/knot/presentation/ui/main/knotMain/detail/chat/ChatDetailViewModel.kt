package com.knot.presentation.ui.main.knotMain.detail.chat

import androidx.lifecycle.viewModelScope
import com.knot.domain.enums.ChatType
import com.knot.domain.usecase.knot.AddChatUseCase
import com.knot.domain.usecase.knot.GetChatListUseCase
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.vo.AddChatRequest
import com.knot.domain.vo.ChatLayoutVo
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TeamStatisticsVo
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.ui.main.knotMain.detail.statistics.StatisticsDetailPageState
import com.knot.presentation.util.DateTimeFormatter
import com.knot.presentation.util.KnotLog
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase,
    private val getChatListUseCase: GetChatListUseCase,
    private val addChatUseCase: AddChatUseCase
) : BaseViewModel<ChatDetailPageState>() {

    private val knotDetailStateFlow : MutableStateFlow<KnotVo> = MutableStateFlow(KnotVo())
    private val chatListStateFlow : MutableStateFlow<List<ChatLayoutVo>> = MutableStateFlow(
        emptyList()
    )
    private val chatTextStateFlow : MutableStateFlow<String> = MutableStateFlow("")

    override val uiState: ChatDetailPageState = ChatDetailPageState(
        knotDetailStateFlow.asStateFlow(),
        chatListStateFlow.asStateFlow(),
        chatTextStateFlow
    )

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

    private fun successGetKnotDetail(result : KnotVo){
        viewModelScope.launch {
            knotDetailStateFlow.update { result }
        }
    }

    private fun getChatList(knotId: String){
        viewModelScope.launch {
            getChatListUseCase(knotId).collect {
                resultResponse(it, ::successGetChatList)
            }
        }
    }

    private fun successGetChatList(result : List<ChatVo>){
        val chatList = getSortedList(result)
        viewModelScope.launch {
            chatListStateFlow.update { addDivideLine(chatList) }
            emitEventFlow(ChatDetailEvent.ScrollDownEvent)
        }
    }

    private fun getSortedList(list : List<ChatVo>) : List<ChatLayoutVo>{
        val chatList = mutableListOf<ChatLayoutVo>()
        list.forEach {
            val type = if(it.id == UserInfo.info.id) ChatType.MY_CHAT
                        else ChatType.OTHER_CHAT
            chatList.add(ChatLayoutVo(type = type, chat = it))
        }

        return chatList
    }

    private fun addDivideLine(list: List<ChatLayoutVo>) : List<ChatLayoutVo>{
        val chatList = mutableListOf<ChatLayoutVo>()
        for(i in list.indices){
            if(i == 0){
                chatList.add(ChatLayoutVo(type = ChatType.DIVIDE, chat = list[i].chat))
                chatList.add(list[i])
            }
            else if(list[i].chat.date != list[i - 1].chat.date){
                chatList.add(ChatLayoutVo(type = ChatType.DIVIDE, chat = list[i].chat))
                chatList.add(list[i])
            }
            else if(list[i].type != ChatType.MY_CHAT && list[i].chat.id == list[i - 1].chat.id){
                chatList.add(ChatLayoutVo(type = ChatType.OTHER_SAME_CHAT, chat = list[i].chat))
            }
            else{
                chatList.add(list[i])
            }
        }

        return chatList
    }

    fun onClickSend(){
        if(chatTextStateFlow.value.trim().isNotEmpty()) {
            val readMap = hashMapOf<String, Boolean>()
            knotDetailStateFlow.value.teamList.forEach {
                if (it.value.id != UserInfo.info.id) readMap[it.value.id] = false
            }
            val request = AddChatRequest(
                knotId = knotDetailStateFlow.value.knotId,
                time = DateTimeFormatter.getNowTimeWithSecond(),
                chat = ChatVo(
                    content = chatTextStateFlow.value,
                    date = DateTimeFormatter.getToday(),
                    id = UserInfo.info.id,
                    name = UserInfo.info.name,
                    readWho = readMap,
                    time = DateTimeFormatter.getNowTimeWithoutSecond()
                )
            )
            viewModelScope.launch {
                addChatUseCase(request).collect {
                    resultResponse(it, {successAddChat()})
                }
            }
        }
    }

    private fun successAddChat(){
        viewModelScope.launch {
            chatTextStateFlow.update { "" }
        }
    }
}