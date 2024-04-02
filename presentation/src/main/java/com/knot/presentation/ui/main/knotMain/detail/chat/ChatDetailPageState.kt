package com.knot.presentation.ui.main.knotMain.detail.chat

import com.knot.domain.vo.ChatLayoutVo
import com.knot.domain.vo.KnotVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ChatDetailPageState(
    val knotDetail: StateFlow<KnotVo>,
    val chatList : StateFlow<List<ChatLayoutVo>>,
    var chatText : MutableStateFlow<String>,
    val insideChatRoomMember : StateFlow<HashMap<String, Boolean>>
) : PageState