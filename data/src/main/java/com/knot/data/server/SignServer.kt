package com.knot.data.server

import android.util.Log
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.knot.data.repository.SignRepositoryImpl
import com.knot.domain.base.Response
import com.knot.domain.resultCode.ResultCode
import com.knot.domain.vo.UserInfoVo
import com.knot.domain.vo.response.KaKaoSignResponseVo
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object SignServer {

    private const val FIREBASE_FUNCTION = "kakaoLogin"

    private val functions = Firebase.functions("asia-northeast3")
    suspend fun kakaoSign(accessToken : String) : Response<KaKaoSignResponseVo> = suspendCoroutine {
        val data = hashMapOf("accessToken" to accessToken)
        functions
            .getHttpsCallable(FIREBASE_FUNCTION)
            .call(data)
            .addOnSuccessListener { result ->
                val info = result.data as Map<String, Any>
                val response = Response(
                    data = KaKaoSignResponseVo(uid = info["uid"].toString(), isNewUser = info["isNewUser"] as Boolean),
                    result = ResultCode.SUCCESS
                )
                it.resume(response)
            }
            .addOnFailureListener { e ->
                it.resume(Response(data = KaKaoSignResponseVo(), result = ResultCode.TEST_ERROR))
            }
    }
}