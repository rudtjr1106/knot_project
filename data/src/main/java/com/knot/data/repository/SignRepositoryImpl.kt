package com.knot.data.repository

import com.knot.data.server.SignServer
import com.knot.domain.base.Response
import com.knot.domain.repository.SignRepository
import com.knot.domain.vo.KaKaoSignResponse
import com.knot.domain.vo.UserVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor() : SignRepository {
    override suspend fun kakaoLogin(request : String): Flow<Response<KaKaoSignResponse>>  = flow {
        emit(SignServer.kakaoSign(request))
    }

    override suspend fun signUp(request: UserVo): Flow<Response<Boolean>> = flow {
        emit(SignServer.signUp(request))
    }

    override suspend fun login(request: String): Flow<Response<Boolean>> = flow {
        emit(SignServer.login(request))
    }

    override suspend fun getMyInfo(): Flow<Response<UserVo>> = flow {
        emit(SignServer.getMyInfo())
    }

    override suspend fun checkAutoLogin(): Flow<Response<Boolean>> = flow {
        emit(SignServer.checkAutoLogin())
    }

    override suspend fun getUserInfo(request: String): Flow<Response<UserVo>> = flow {
        emit(SignServer.getUserInfo(request))
    }

}