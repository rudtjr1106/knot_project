package com.knot.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.knot.domain.base.Response
import com.knot.domain.repository.SignRepository
import com.knot.domain.vo.UserInfoVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor() : SignRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private lateinit var fcmToken : String

    override suspend fun kakaoLogin(): Flow<Response<UserInfoVo>>  = flow {
        Log.d("하이", "하이")
        val response = Response(
            data = UserInfoVo(nickName = "테스트", uid = "테스트uid"),
            error = "성공"
        )
        emit(response)
    }

}