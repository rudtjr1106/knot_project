package com.knot.data.server

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.knot.data.Endpoints
import com.knot.domain.base.Response
import com.knot.domain.resultCode.ResultCode
import com.knot.domain.vo.KaKaoSignResponse
import com.knot.domain.vo.UserVo
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object SignServer {

    private const val FIREBASE_FUNCTION = "kakaoLogin"
    private val db = FirebaseDatabase.getInstance()
    private val authRef = db.getReference(Endpoints.USER)
    private val auth = FirebaseAuth.getInstance()

    private val functions = Firebase.functions("asia-northeast3")
    suspend fun kakaoSign(accessToken: String): Response<KaKaoSignResponse> = suspendCoroutine {
        val data = hashMapOf("accessToken" to accessToken)
        functions
            .getHttpsCallable(FIREBASE_FUNCTION)
            .call(data)
            .addOnSuccessListener { result ->
                val info = result.data as Map<String, Any>
                val response = Response(
                    data = KaKaoSignResponse(
                        uid = info["uid"].toString(),
                        isNewUser = info["isNewUser"] as Boolean,
                        token = info["token"].toString()
                    ),
                    result = ResultCode.SUCCESS
                )
                it.resume(response)
            }
            .addOnFailureListener { e ->
                it.resume(Response(data = KaKaoSignResponse(), result = ResultCode.TEST_ERROR))
            }
    }

    suspend fun signUp(request: UserVo): Response<Boolean> = suspendCoroutine {
        val requestVo = request.copy(
            uid = auth.uid.toString()
        )
        authRef.child(auth.uid.toString()).setValue(requestVo).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                it.resume(Response(data = true, result = ResultCode.SUCCESS))
            } else {
                it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
            }
        }
    }

    suspend fun login(token: String): Response<Boolean> = suspendCoroutine {
        if (token.isNullOrEmpty()) {
            it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
        } else {
            auth.signInWithCustomToken(token)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        it.resume(Response(data = true, result = ResultCode.SUCCESS))
                    } else {
                        it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                    }
                }
        }
    }

    suspend fun getMyInfo(): Response<UserVo> = suspendCoroutine { coroutineScope ->
        authRef.child(auth.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userVo = snapshot.getValue(UserVo::class.java)
                        userVo?.let {
                            coroutineScope.resume(Response(data = it, result = ResultCode.SUCCESS))
                        }
                    } else {
                        coroutineScope.resume(
                            Response(
                                data = UserVo(),
                                result = ResultCode.TEST_ERROR
                            )
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    coroutineScope.resume(
                        Response(
                            data = UserVo(),
                            result = ResultCode.TEST_ERROR
                        )
                    )
                }
            })
    }

    suspend fun checkAutoLogin(): Response<Boolean> = suspendCoroutine {
        var isLogin = auth.currentUser?.uid != null
        if(isLogin){
            it.resume(Response(data = isLogin, result = ResultCode.SUCCESS))
        }
        else{
            it.resume(Response(data = isLogin, result = ResultCode.TEST_ERROR))
        }
    }

    suspend fun getUserInfo(request : String): Response<UserVo> = suspendCoroutine { coroutineScope ->
        authRef.orderByChild(Endpoints.USER_ID).equalTo(request).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val userData = data.getValue(UserVo::class.java)
                        userData?.let {
                            coroutineScope.resume(Response(data = userData, result = ResultCode.SUCCESS))
                        }
                    }
                } else {
                    coroutineScope.resume(Response(data = UserVo(), result = ResultCode.TEST_ERROR))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                coroutineScope.resume(Response(data = UserVo(), result = ResultCode.TEST_ERROR))
            }
        })
    }
}