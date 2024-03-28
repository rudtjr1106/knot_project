package com.knot.data.server

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.knot.data.Endpoints
import com.knot.domain.base.Response
import com.knot.domain.resultCode.ResultCode
import com.knot.domain.vo.ChatListVo
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.UserVo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object KnotServer {

    private val db = FirebaseDatabase.getInstance()
    private val knotRef = db.getReference(Endpoints.KNOT)
    private val chatRef = db.getReference(Endpoints.CHAT)

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

    suspend fun getChatList(knotId: String) : Flow<Response<ChatListVo>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val chatList: MutableList<ChatVo> = mutableListOf()
                    for (dataSnapshot in snapshot.children) {
                        val chatVo = dataSnapshot.getValue(ChatVo::class.java)
                        chatVo?.let {
                            chatList.add(it)
                        }
                    }
                    val response = Response(data = ChatListVo(chatList), result = ResultCode.SUCCESS)
                    trySend(response)
                }
                else{
                    trySend(Response(data = ChatListVo(), result = ResultCode.TEST_ERROR))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response(data = ChatListVo(), result = ResultCode.TEST_ERROR))
            }
        }
        chatRef.child(knotId).addValueEventListener(listener)
        awaitClose { chatRef.child(knotId).removeEventListener(listener) }
    }
}