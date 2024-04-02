package com.knot.domain.usecase.knot

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.AddChatRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddChatUseCase @Inject constructor(
    private val knotRepository: KnotRepository
) : UseCase<AddChatRequest, Boolean>() {
    override suspend operator fun invoke(request: AddChatRequest): Flow<Response<Boolean>> {
        return knotRepository.addChat(request)
    }
}