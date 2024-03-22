package com.knot.domain.repository

import com.knot.domain.base.Response
import com.knot.domain.vo.KaKaoSignResponse
import com.knot.domain.vo.UserVo
import kotlinx.coroutines.flow.Flow

interface SignRepository {
    suspend fun kakaoLogin(request : String) : Flow<Response<KaKaoSignResponse>>
    suspend fun signUp(request: UserVo) : Flow<Response<Boolean>>
    suspend fun login(request: String): Flow<Response<Boolean>>
    suspend fun getMyInfo() : Flow<Response<UserVo>>
    suspend fun checkAutoLogin() : Flow<Response<Boolean>>
}