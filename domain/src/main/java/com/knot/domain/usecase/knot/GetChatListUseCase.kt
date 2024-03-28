package com.knot.domain.usecase.knot

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.ChatListVo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatListUseCase @Inject constructor(
    private val knotRepository: KnotRepository
) : UseCase<String, ChatListVo>() {
    override suspend operator fun invoke(request: String): Flow<Response<ChatListVo>> {
        return knotRepository.getChatList(request)
    }
}