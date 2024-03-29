package com.knot.data.repository

import com.knot.data.server.KnotServer
import com.knot.domain.base.Response
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.ChatListVo
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.KnotVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KnotRepositoryImpl @Inject constructor() : KnotRepository {
    override suspend fun getKnot(request: String): Flow<Response<KnotVo>> = flow {
        emit(KnotServer.getKnot(request))
    }

    override suspend fun getChatList(request: String): Flow<Response<ChatListVo>> = flow {
        KnotServer.getChatList(request).collect{
            emit(it)
        }
    }

    override suspend fun getMyKnotList(): Flow<Response<List<KnotVo>>> = flow {
        emit(KnotServer.getMyKnotList())
    }

    override suspend fun checkKnotTodo(request: CheckKnotTodoRequest): Flow<Response<Boolean>> = flow {
        emit(KnotServer.checkKnotTodo(request))
    }
}