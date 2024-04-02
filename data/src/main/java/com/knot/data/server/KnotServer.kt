package com.knot.data.server

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.knot.data.Endpoints
import com.knot.domain.base.Response
import com.knot.domain.resultCode.ResultCode
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.KnotVo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object KnotServer {

    private val db = FirebaseDatabase.getInstance()
    private val knotRef = db.getReference(Endpoints.KNOT)
    private val chatRef = db.getReference(Endpoints.CHAT)
    private val authRef = db.getReference(Endpoints.USER)
    private val auth = FirebaseAuth.getInstance()

    suspend fun getMyKnotList(): Response<List<KnotVo>> = suspendCoroutine { coroutineScope ->
        val knotList = mutableListOf<KnotVo>()
        authRef.child(auth.uid.toString()).child(Endpoints.KNOT)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (dataSnapshot in snapshot.children) {
                            val knotVo = dataSnapshot.getValue(KnotVo::class.java)
                            knotVo?.let {
                                knotList.add(it)
                            }
                        }
                        coroutineScope.resume(Response(data = knotList, result = ResultCode.SUCCESS))
                    } else {
                        coroutineScope.resume(Response(data = knotList, result = ResultCode.TEST_ERROR))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    coroutineScope.resume(Response(data = knotList, result = ResultCode.TEST_ERROR))
                }
            })
    }

    suspend fun getKnot(knotId : String): Response<KnotVo> = suspendCoroutine { coroutineScope ->
        knotRef.child(knotId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val knotVo = snapshot.getValue(KnotVo::class.java)
                        knotVo?.let {
                            coroutineScope.resume(Response(data = it, result = ResultCode.SUCCESS))
                        }
                    } else {
                        coroutineScope.resume(Response(data = KnotVo(), result = ResultCode.TEST_ERROR))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    coroutineScope.resume(Response(data = KnotVo(), result = ResultCode.TEST_ERROR))
                }
            })
    }

    suspend fun getChatList(knotId: String) : Flow<Response<List<ChatVo>>> = callbackFlow {
        val chatList = mutableListOf<ChatVo>()
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (dataSnapshot in snapshot.children) {
                        dataSnapshot.children.forEach {
                            val chatVo = it.getValue(ChatVo::class.java)
                            chatVo?.let { chat -> chatList.add(chat) }
                        }
                    }
                    trySend(Response(data = chatList, result = ResultCode.SUCCESS))
                }
                else{
                    trySend(Response(data = chatList, result = ResultCode.TEST_ERROR))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response(data = chatList, result = ResultCode.TEST_ERROR))
            }
        }
        chatRef.child(knotId).addValueEventListener(listener)
        awaitClose { chatRef.child(knotId).removeEventListener(listener) }
    }

    suspend fun checkKnotTodo(request: CheckKnotTodoRequest) : Response<Boolean> = suspendCoroutine { continuation ->
        knotRef.child(request.knotId).child(Endpoints.KNOT_TODO).child(request.todoId).child(Endpoints.TODO_COMPLETE).setValue(request.complete)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    checkMyKnotTodo(request){ isSuccess ->
                        if(isSuccess){
                            continuation.resume(Response(data = true, result = ResultCode.SUCCESS))
                        }
                        else{
                            continuation.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                        }
                    }
                }
                else{
                    continuation.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                }
            }
    }

    private fun checkMyKnotTodo(request: CheckKnotTodoRequest, callback : (Boolean) -> Unit) {
        authRef.child(auth.uid.toString()).child(Endpoints.KNOT)
            .child(request.knotId).child(Endpoints.KNOT_TODO).child(request.todoId).child(Endpoints.TODO_COMPLETE).setValue(request.complete)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    callback(true)
                }
                else{
                    callback(false)
                }
            }
    }
}