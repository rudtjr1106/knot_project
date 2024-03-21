package com.knot.domain.repository

import com.knot.domain.base.Response
import com.knot.domain.vo.request.SignUpRequest
import com.knot.domain.vo.response.GetMyInfoResponse
import com.knot.domain.vo.response.KaKaoSignResponse
import kotlinx.coroutines.flow.Flow

interface SignRepository {
    suspend fun kakaoLogin(request : String) : Flow<Response<KaKaoSignResponse>>
    suspend fun signUp(request: SignUpRequest) : Flow<Response<Boolean>>
    suspend fun login(request: String): Flow<Response<Boolean>>
    suspend fun getMyInfo() : Flow<Response<GetMyInfoResponse>>
    suspend fun checkAutoLogin() : Flow<Response<Boolean>>
}