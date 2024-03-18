package com.knot.domain.repository

import com.knot.domain.base.Response
import com.knot.domain.vo.request.SignUpRequest
import com.knot.domain.vo.response.KaKaoSignResponseVo
import kotlinx.coroutines.flow.Flow

interface SignRepository {
    suspend fun kakaoLogin(request : String) : Flow<Response<KaKaoSignResponseVo>>
    suspend fun signUp(request: SignUpRequest) : Flow<Response<Boolean>>
    suspend fun login(request: String): Flow<Response<Boolean>>
}