package com.knot.domain.repository

import com.knot.domain.base.Response
import com.knot.domain.vo.AddChatRequest
import com.knot.domain.vo.ApplyKnotRequest
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.InsideChatRequest
import com.knot.domain.vo.InsideChatResponse
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.RejectOrApproveTeamRequest
import com.knot.domain.vo.SaveRoleAndRuleRequest
import com.knot.domain.vo.SearchKnotRequest
import kotlinx.coroutines.flow.Flow

interface KnotRepository {
    suspend fun getKnot(request : String) : Flow<Response<KnotVo>>
    suspend fun getKnotList(request : SearchKnotRequest) : Flow<Response<List<KnotVo>>>
    suspend fun getChatList(request: String) : Flow<Response<List<ChatVo>>>
    suspend fun getMyKnotList() : Flow<Response<List<KnotVo>>>
    suspend fun checkKnotTodo(request: CheckKnotTodoRequest) : Flow<Response<Boolean>>
    suspend fun addChat(request: AddChatRequest) : Flow<Response<Boolean>>
    suspend fun insideChat(request: InsideChatRequest) : Flow<Response<InsideChatResponse>>
    suspend fun saveRoleAndRule(request : SaveRoleAndRuleRequest) : Flow<Response<Boolean>>
    suspend fun editKnot(request: KnotVo) : Flow<Response<Boolean>>
    suspend fun createKnot(request: KnotVo) : Flow<Response<Boolean>>
    suspend fun applyKnot(request : ApplyKnotRequest) : Flow<Response<Boolean>>
    suspend fun cancelApplicationKnot(request: String) : Flow<Response<Boolean>>
    suspend fun approveKnotApplicant(request : RejectOrApproveTeamRequest) : Flow<Response<Boolean>>
    suspend fun rejectKnotApplicant(request : RejectOrApproveTeamRequest) : Flow<Response<Boolean>>
    suspend fun deleteKnot(request: String) : Flow<Response<Boolean>>
    suspend fun outKnot(request: KnotVo) : Flow<Response<Boolean>>
}