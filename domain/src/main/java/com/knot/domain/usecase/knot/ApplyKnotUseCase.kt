package com.knot.domain.usecase.knot

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.ApplyKnotRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApplyKnotUseCase @Inject constructor(
    private val knotRepository: KnotRepository
) : UseCase<ApplyKnotRequest, Boolean>() {
    override suspend operator fun invoke(request: ApplyKnotRequest): Flow<Response<Boolean>> {
        return knotRepository.applyKnot(request)
    }
}