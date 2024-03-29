package com.knot.domain.usecase.knot

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.ChatVo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatListUseCase @Inject constructor(
    private val knotRepository: KnotRepository
) : UseCase<String, List<ChatVo>>() {
    override suspend operator fun invoke(request: String): Flow<Response<List<ChatVo>>> {
        return knotRepository.getChatList(request)
    }
}