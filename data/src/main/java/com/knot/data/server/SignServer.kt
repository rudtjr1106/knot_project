package com.knot.data.server

import android.util.Log
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.knot.data.repository.SignRepositoryImpl
import com.knot.domain.base.Response
import com.knot.domain.resultCode.ResultCode
import com.knot.domain.vo.UserInfoVo
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object SignServer {

    private const val FIREBASE_FUNCTION = "kakaoLogin"

    private val functions = Firebase.functions("asia-northeast3")
    suspend fun kakaoSign(accessToken : String) : Response<UserInfoVo> = suspendCoroutine {
        val data = hashMapOf(
            "accessToken" to accessToken
        )

        // Firebase Functions 호출
        functions
            .getHttpsCallable(FIREBASE_FUNCTION)
            .call(data)
            .addOnSuccessListener { result ->
                // Firebase Functions 호출 성공 시 처리
                val uid = result.data as Map<String, Any>
                val response = Response(
                    data = UserInfoVo(nickName = "테스트", uid = "테스트uid"),
                    result = ResultCode.SUCCESS
                )
                it.resume(response)
            }
            .addOnFailureListener { e ->
                it.resume(Response(data = UserInfoVo(), result = ResultCode.TEST_ERROR))
            }
    }
}