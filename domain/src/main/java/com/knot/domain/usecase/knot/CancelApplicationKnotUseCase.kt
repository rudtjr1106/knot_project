package com.knot.domain.usecase.knot

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.KnotRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CancelApplicationKnotUseCase @Inject constructor(
    private val knotRepository: KnotRepository
) : UseCase<String, Boolean>() {
    override suspend operator fun invoke(request: String): Flow<Response<Boolean>> {
        return knotRepository.cancelApplicationKnot(request)
    }
}