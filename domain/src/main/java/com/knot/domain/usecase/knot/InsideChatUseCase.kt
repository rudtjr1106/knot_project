package com.knot.domain.usecase.knot

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.InsideChatRequest
import com.knot.domain.vo.InsideChatResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsideChatUseCase @Inject constructor(
    private val knotRepository: KnotRepository
) : UseCase<InsideChatRequest, InsideChatResponse>() {
    override suspend operator fun invoke(request: InsideChatRequest): Flow<Response<InsideChatResponse>> {
        return knotRepository.insideChat(request)
    }
}