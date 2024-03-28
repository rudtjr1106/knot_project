package com.knot.domain.repository

import com.knot.domain.base.Response
import com.knot.domain.vo.ChatListVo
import com.knot.domain.vo.KnotVo
import kotlinx.coroutines.flow.Flow

interface KnotRepository {
    suspend fun getKnot(request : String) : Flow<Response<KnotVo>>
    suspend fun getChatList(request: String) : Flow<Response<ChatListVo>>
}