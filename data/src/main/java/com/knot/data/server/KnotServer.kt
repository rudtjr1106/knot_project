package com.knot.data.server

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.knot.data.Endpoints
import com.knot.domain.base.Response
import com.knot.domain.resultCode.ResultCode
import com.knot.domain.vo.AddChatRequest
import com.knot.domain.vo.ApplyKnotRequest
import com.knot.domain.vo.ChatVo
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.InsideChatRequest
import com.knot.domain.vo.InsideChatResponse
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.RejectOrApproveTeamRequest
import com.knot.domain.vo.SaveRoleAndRuleRequest
import com.knot.domain.vo.SearchKnotRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object KnotServer {

    private val db = FirebaseDatabase.getInstance()
    private val knotRef = db.getReference(Endpoints.KNOT)
    private val chatRef = db.getReference(Endpoints.CHAT)
    private val chatMemberRef = db.getReference(Endpoints.CHAT_MEMBER)
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

    suspend fun getKnotList(request : SearchKnotRequest): Response<List<KnotVo>> = suspendCoroutine { coroutineScope ->
        var knotList : List<KnotVo>
        knotRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        knotList = getSearchedKnot(request, snapshot)
                        coroutineScope.resume(Response(data = knotList, result = ResultCode.SUCCESS))
                    } else {
                        coroutineScope.resume(Response(data = emptyList(), result = ResultCode.TEST_ERROR))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    coroutineScope.resume(Response(data = emptyList(), result = ResultCode.TEST_ERROR))
                }
            })
    }

    private fun getSearchedKnot(request : SearchKnotRequest ,snapshot: DataSnapshot) : List<KnotVo>{
        val list = mutableListOf<KnotVo>()
        for (dataSnapshot in snapshot.children) {
            val knotVo = dataSnapshot.getValue(KnotVo::class.java)
            knotVo?.let {
                if(!it.privateKnot){
                    if(request.category.isEmpty() && request.searchContent.isEmpty()) list.add(it)
                    if(request.searchContent.isNotEmpty()) {
                        if(it.title.contains(request.searchContent) || it.content.contains(request.searchContent)) list.add(it)
                    }
                    if(request.category.isNotEmpty()) {
                        if(it.category[request.category] == true) list.add(it)
                    }
                }
            }
        }
        return list
    }

    suspend fun getChatList(knotId: String) : Flow<Response<List<ChatVo>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatList = mutableListOf<ChatVo>()
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
                trySend(Response(data = emptyList(), result = ResultCode.TEST_ERROR))
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

    suspend fun addChat(request : AddChatRequest) : Response<Boolean> = suspendCoroutine {
        val key = request.time + "-" + request.chat.id
        chatRef.child(request.knotId).child(request.chat.date).child(key).setValue(request.chat)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    it.resume(Response(data = true, result = ResultCode.SUCCESS))
                }
                else{
                    it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                }
        }
    }

    suspend fun insideChat(request : InsideChatRequest) : Flow<Response<InsideChatResponse>> = callbackFlow {
        if (request.isInside) readAllChat(request)
        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val map = hashMapOf<String, Boolean>()
                for (dataSnapshot in snapshot.children) {
                    if(dataSnapshot.key.toString() != request.id){
                        map[dataSnapshot.key.toString()] = dataSnapshot.value.toString().toBoolean()
                    }
                }
                trySend(Response(data = InsideChatResponse(map), result = ResultCode.SUCCESS))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response(data = InsideChatResponse(), result = ResultCode.TEST_ERROR))
            }
        }
        chatMemberRef.child(request.knotId).child(request.id).setValue(request.isInside)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    chatMemberRef.child(request.knotId).addValueEventListener(listener)
                }
                else{
                    trySend(Response(data = InsideChatResponse(), result = ResultCode.TEST_ERROR))
                }
            }
        awaitClose { chatMemberRef.child(request.knotId).removeEventListener(listener) }
    }

    private fun readAllChat(request : InsideChatRequest){
        chatRef.child(request.knotId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.children.forEach {
                        val chatVo = it.getValue(ChatVo::class.java)
                        if (chatVo != null) {
                            if(chatVo.id != request.id){
                                chatRef.child(request.knotId).child(dataSnapshot.key.toString()).child(it.key.toString())
                                    .child(Endpoints.CHAT_READ_MEMBER).child(request.id).setValue(request.isInside)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    suspend fun saveRoleAndRule(request : SaveRoleAndRuleRequest) : Response<Boolean> = suspendCoroutine {
        knotRef.child(request.knotId).child(Endpoints.KNOT_TEAM).setValue(request.teamUserMap)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    saveRule(request) { isSuccess ->
                        if(isSuccess) it.resume(Response(data = true, result = ResultCode.SUCCESS))
                        else it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                    }
                }
                else{
                    it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                }
            }
    }

    private fun saveRule(request: SaveRoleAndRuleRequest, callback : (Boolean) -> Unit) {
        knotRef.child(request.knotId).child(Endpoints.KNOT_RULE).setValue(request.rule)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    callback(true)
                }
                else{
                    callback(false)
                }
            }
    }

    suspend fun editKnot(request : KnotVo) : Response<Boolean> = suspendCoroutine {
        knotRef.child(request.knotId).setValue(request)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    request.teamList.forEach { editUserKnot(request, it.value.uid) }
                    it.resume(Response(data = true, result = ResultCode.SUCCESS))
                }
                else{
                    it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                }
            }
    }

    suspend fun createKnot(request : KnotVo) : Response<Boolean> = suspendCoroutine {
        var lastProseId = 1
        knotRef.orderByChild(Endpoints.KNOT_ID).limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val knotVo = snapshot.getValue(KnotVo::class.java)
                        knotVo?.let { knot ->
                            lastProseId = extractNumberFromString(knot.knotId)?.plus(1) ?: -1
                        }
                    }
                    val newRequest = request.copy(knotId = lastProseId.toString() + "번")

                    db.getReference(Endpoints.KNOT).child(newRequest.knotId)
                        .setValue(newRequest)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                addMyKnot(newRequest) { isSuccess ->
                                    if(isSuccess) it.resume(Response(data = true, result = ResultCode.SUCCESS))
                                    else it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                                }
                            } else {
                                it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                            }
                        }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                }
            })
    }

    private fun addMyKnot(knot: KnotVo, callback: (Boolean) -> Unit) {
        authRef.child(auth.uid.toString()).child(Endpoints.KNOT).child(knot.knotId).setValue(knot)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
    }

    private fun editUserKnot(knot: KnotVo, uid: String) {
        authRef.child(uid).child(Endpoints.KNOT).child(knot.knotId).setValue(knot)
    }

    suspend fun applyKnot(request: ApplyKnotRequest) : Response<Boolean> = suspendCoroutine {
        val newRequest = hashMapOf("moreInfo" to request.moreIntro, "user" to request.userVo)
        knotRef.child(request.knotId).child(Endpoints.KNOT_APPLY).child(request.userVo.uid).setValue(newRequest)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    addMyApplyKnot(request)
                    it.resume(Response(data = true, result = ResultCode.SUCCESS))
                }
                else{
                    it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                }
            }
    }

    private fun addMyApplyKnot(request: ApplyKnotRequest){
        val newRequest = hashMapOf("moreInfo" to request.moreIntro, "knotTitle" to request.knotTitle, "knotId" to request.knotId)
        authRef.child(request.userVo.uid).child(Endpoints.USER_APPLY_LIST).child(request.knotId).setValue(newRequest)
    }

    suspend fun cancelApplicationKnot(request : String) : Response<Boolean> = suspendCoroutine {
        knotRef.child(request).child(Endpoints.KNOT_APPLY).child(auth.uid.toString()).removeValue()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    cancelMyApplyKnot(request)
                    it.resume(Response(data = true, result = ResultCode.SUCCESS))
                }
                else{
                    it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                }
            }
    }

    private fun cancelMyApplyKnot(request: String){
        authRef.child(auth.uid.toString()).child(Endpoints.USER_APPLY_LIST).child(request).removeValue()
    }

    suspend fun approveKnotApplicant(request: RejectOrApproveTeamRequest) : Response<Boolean> = suspendCoroutine {
        knotRef.child(request.knot.knotId).child(Endpoints.KNOT_APPLY).child(request.teamUserVo.uid).removeValue()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    addTeamInKnot(request)
                    removeMyApplication(request)
                    it.resume(Response(data = true, result = ResultCode.SUCCESS))
                }
                else{
                    it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                }
            }
    }

    private fun addTeamInKnot(request : RejectOrApproveTeamRequest) {
        knotRef.child(request.knot.knotId).child(Endpoints.KNOT_TEAM).child(request.teamUserVo.uid).setValue(request.teamUserVo).addOnCompleteListener {
            if(it.isSuccessful){
                val newMap = request.knot.teamList
                newMap[request.teamUserVo.uid] = request.teamUserVo
                val newRequest = request.knot.copy(
                    teamList = newMap
                )
                request.knot.teamList.forEach { updateKnotInTeam(it.value.uid, newRequest) }
            }
        }
    }

    private fun updateKnotInTeam(uid : String, knotVo: KnotVo){
        authRef.child(uid).child(Endpoints.KNOT).child(knotVo.knotId).setValue(knotVo)
    }

    private fun removeMyApplication(request: RejectOrApproveTeamRequest){
        authRef.child(request.teamUserVo.uid).child(Endpoints.USER_APPLY_LIST).child(request.knot.knotId).removeValue()
    }

    suspend fun rejectKnotApplicant(request: RejectOrApproveTeamRequest) : Response<Boolean> = suspendCoroutine {
        knotRef.child(request.knot.knotId).child(Endpoints.KNOT_APPLY).child(request.teamUserVo.uid).removeValue()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    removeMyApplication(request)
                    it.resume(Response(data = true, result = ResultCode.SUCCESS))
                }
                else{
                    it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
                }
            }
    }

    suspend fun deleteKnot(request: String) : Response<Boolean> = suspendCoroutine {
        knotRef.child(request).removeValue().addOnCompleteListener { task ->
            if(task.isSuccessful){
                deleteMyKnot(request)
                deleteChatRoom(request)
                it.resume(Response(data = true, result = ResultCode.SUCCESS))
            }
            else{
                it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
            }
        }
    }

    private fun deleteChatRoom(request: String) {
        chatRef.child(request).removeValue()
        chatMemberRef.child(request).removeValue()
    }

    private fun deleteMyKnot(request: String) {
        authRef.child(auth.uid.toString()).child(Endpoints.KNOT).child(request).removeValue()
    }

    suspend fun outKnot(request: KnotVo) : Response<Boolean> = suspendCoroutine {
        knotRef.child(request.knotId).child(Endpoints.KNOT_TEAM).child(auth.uid.toString()).removeValue().addOnCompleteListener { task ->
            if(task.isSuccessful){
                request.teamList.values.forEach {
                    if(it.uid != auth.uid.toString()) deleteMeInOtherKnot(it.uid, request.knotId)
                }
                deleteMyKnot(request.knotId)
                it.resume(Response(data = true, result = ResultCode.SUCCESS))
            }
            else{
                it.resume(Response(data = false, result = ResultCode.TEST_ERROR))
            }
        }
    }

    private fun deleteMeInOtherKnot(uid: String, knotId : String) {
        authRef.child(uid).child(Endpoints.KNOT).child(knotId).child(Endpoints.KNOT_TEAM).child(auth.uid.toString()).removeValue()
    }

    private fun extractNumberFromString(input: String): Int? {
        val regex = Regex("\\d+")
        val matchResult = regex.find(input)
        return matchResult?.value?.toInt()
    }
}