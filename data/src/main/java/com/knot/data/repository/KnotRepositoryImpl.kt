package com.knot.data.repository

import com.knot.data.server.KnotServer
import com.knot.domain.base.Response
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.AddChatRequest
import com.knot.domain.vo.ApplyKnotRequest
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.InsideChatRequest
import com.knot.domain.vo.InsideChatResponse
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.SaveRoleAndRuleRequest
import com.knot.domain.vo.SearchKnotRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KnotRepositoryImpl @Inject constructor() : KnotRepository {
    override suspend fun getKnot(request: String): Flow<Response<KnotVo>> = flow {
        emit(KnotServer.getKnot(request))
    }

    override suspend fun getKnotList(request: SearchKnotRequest): Flow<Response<List<KnotVo>>> = flow {
        emit(KnotServer.getKnotList(request))
    }


    override suspend fun getChatList(request: String): Flow<Response<List<ChatVo>>> = flow {
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

    override suspend fun addChat(request: AddChatRequest): Flow<Response<Boolean>> = flow {
        emit(KnotServer.addChat(request))
    }

    override suspend fun insideChat(request: InsideChatRequest): Flow<Response<InsideChatResponse>> = flow {
        KnotServer.insideChat(request).collect{
            emit(it)
        }
    }

    override suspend fun saveRoleAndRule(request: SaveRoleAndRuleRequest): Flow<Response<Boolean>> = flow {
        emit(KnotServer.saveRoleAndRule(request))
    }

    override suspend fun editKnot(request: KnotVo): Flow<Response<Boolean>> = flow {
        emit(KnotServer.editKnot(request))
    }

    override suspend fun createKnot(request: KnotVo): Flow<Response<Boolean>> = flow {
        emit(KnotServer.createKnot(request))
    }

    override suspend fun applyKnot(request: ApplyKnotRequest): Flow<Response<Boolean>> = flow {
        emit(KnotServer.applyKnot(request))
    }
}