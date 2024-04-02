package com.knot.presentation.ui.main.knotMain.detail.chat

import androidx.lifecycle.viewModelScope
import com.knot.domain.enums.ChatType
import com.knot.domain.usecase.knot.AddChatUseCase
import com.knot.domain.usecase.knot.GetChatListUseCase
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.usecase.knot.InsideChatUseCase
import com.knot.domain.vo.AddChatRequest
import com.knot.domain.vo.ChatLayoutVo
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.InsideChatRequest
import com.knot.domain.vo.InsideChatResponse
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
    private val addChatUseCase: AddChatUseCase,
    private val insideChatUseCase: InsideChatUseCase,
) : BaseViewModel<ChatDetailPageState>() {

    private val knotDetailStateFlow : MutableStateFlow<KnotVo> = MutableStateFlow(KnotVo())
    private val chatListStateFlow : MutableStateFlow<List<ChatLayoutVo>> = MutableStateFlow(
        emptyList()
    )
    private val chatTextStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val insideChatRoomMemberStateFlow : MutableStateFlow<HashMap<String, Boolean>> = MutableStateFlow(
        hashMapOf()
    )

    override val uiState: ChatDetailPageState = ChatDetailPageState(
        knotDetailStateFlow.asStateFlow(),
        chatListStateFlow.asStateFlow(),
        chatTextStateFlow,
        insideChatRoomMemberStateFlow.asStateFlow()
    )

    fun getDetail(knotId : String){
        insideChatRoom(knotId, true)
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
        }
    }

    private fun insideChatRoom(knotId: String, isInside : Boolean){
        val request = InsideChatRequest(knotId = knotId, id = UserInfo.info.id, isInside = isInside)
        viewModelScope.launch {
            insideChatUseCase(request).collect{
                resultResponse(it, ::successInsideChatRoom)
            }
        }
    }

    private fun successInsideChatRoom(result : InsideChatResponse){
        viewModelScope.launch {
            insideChatRoomMemberStateFlow.update { result.member }
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
            else if(isDifferentDate(list[i], list[i - 1])){
                chatList.add(ChatLayoutVo(type = ChatType.DIVIDE, chat = list[i].chat))
                chatList.add(list[i])
            }
            else if(i != list.lastIndex && isOtherChatSameTime(list[i], list[i + 1])){
                chatList.add(ChatLayoutVo(type = ChatType.OTHER_SAME_TIME_CHAT, chat = list[i].chat))
            }
            else if(isOtherChatSame(list[i], list[i - 1])){
                chatList.add(ChatLayoutVo(type = ChatType.OTHER_SAME_CHAT, chat = list[i].chat))
            }
            else if(i != list.lastIndex && isMyChatSameTime(list[i], list[i + 1])){
                chatList.add(ChatLayoutVo(type = ChatType.MY_SAME_TIME_CHAT, chat = list[i].chat))
            }
            else{
                chatList.add(list[i])
            }
        }

        return chatList
    }

    private fun isDifferentDate(compare1 : ChatLayoutVo, compare2: ChatLayoutVo) : Boolean{
        return compare1.chat.date != compare2.chat.date
    }

    private fun isOtherChatSameTime(compare1 : ChatLayoutVo, compare2: ChatLayoutVo) : Boolean{
        return compare1.type != ChatType.MY_CHAT && compare1.chat.id == compare2.chat.id
                && compare1.chat.time == compare2.chat.time
    }

    private fun isOtherChatSame(compare1 : ChatLayoutVo, compare2: ChatLayoutVo) : Boolean{
        return compare1.type != ChatType.MY_CHAT && compare1.chat.id == compare2.chat.id
    }

    private fun isMyChatSameTime(compare1 : ChatLayoutVo, compare2: ChatLayoutVo) : Boolean{
        return compare1.type == ChatType.MY_CHAT && compare1.chat.id == compare2.chat.id
                && compare1.chat.time == compare2.chat.time
    }

    fun onClickSend(){
        if(chatTextStateFlow.value.trim().isNotEmpty()) {
            val request = AddChatRequest(
                knotId = knotDetailStateFlow.value.knotId,
                time = DateTimeFormatter.getNowTimeWithSecond(),
                chat = ChatVo(
                    content = chatTextStateFlow.value,
                    date = DateTimeFormatter.getToday(),
                    id = UserInfo.info.id,
                    name = UserInfo.info.name,
                    readWho = insideChatRoomMemberStateFlow.value,
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

    fun outChatRoom(knotId: String){
        insideChatRoom(knotId, false)
    }
}