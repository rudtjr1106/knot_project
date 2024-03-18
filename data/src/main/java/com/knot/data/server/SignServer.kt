package com.knot.data.server

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.knot.data.Endpoints
import com.knot.domain.base.Response
import com.knot.domain.resultCode.ResultCode
import com.knot.domain.vo.request.SignUpRequest
import com.knot.domain.vo.response.KaKaoSignResponseVo
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object SignServer {

    private const val FIREBASE_FUNCTION = "kakaoLogin"
    private val db = FirebaseDatabase.getInstance()
    private val authRef = db.getReference(Endpoints.USER)
    private val auth = FirebaseAuth.getInstance()

    private val functions = Firebase.functions("asia-northeast3")
    suspend fun kakaoSign(accessToken : String) : Response<KaKaoSignResponseVo> = suspendCoroutine {
        val data = hashMapOf("accessToken" to accessToken)
        functions
            .getHttpsCallable(FIREBASE_FUNCTION)
            .call(data)
            .addOnSuccessListener { result ->
                val info = result.data as Map<String, Any>
                val response = Response(
                    data = KaKaoSignResponseVo(uid = info["uid"].toString(), isNewUser = info["isNewUser"] as Boolean, token = info["token"].toString()),
                    result = ResultCode.SUCCESS
                )
                it.resume(response)
            }
            .addOnFailureListener { e ->
                it.resume(Response(data = KaKaoSignResponseVo(), result = ResultCode.TEST_ERROR))
            }
    }

    suspend fun signUp(request: SignUpRequest) : Response<Boolean> = suspendCoroutine {
        authRef.child(auth.uid.toString()).setValue(request).addOnCompleteListener { task ->
            if(task.isSuccessful){
                it.resume(Response(data = true, result = ResultCode.SUCCESS))
            }
            else{
                it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
            }
        }
    }

    suspend fun login(token : String) : Response<Boolean> = suspendCoroutine {
        auth.signInWithCustomToken(token)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    it.resume(Response(data = true, result = ResultCode.SUCCESS))
                }
                else{
                    it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                }
            }
    }
}