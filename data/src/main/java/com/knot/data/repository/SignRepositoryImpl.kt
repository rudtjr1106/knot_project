package com.knot.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.knot.data.server.SignServer
import com.knot.domain.base.Response
import com.knot.domain.repository.SignRepository
import com.knot.domain.resultCode.ResultCode
import com.knot.domain.vo.UserInfoVo
import com.knot.domain.vo.response.KaKaoSignResponseVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor() : SignRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()

    override suspend fun kakaoLogin(request : String): Flow<Response<KaKaoSignResponseVo>>  = flow {
        emit(SignServer.kakaoSign(request))
    }

}