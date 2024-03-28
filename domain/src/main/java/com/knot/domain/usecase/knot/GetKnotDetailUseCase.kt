package com.knot.domain.usecase.knot

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.KnotVo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetKnotDetailUseCase @Inject constructor(
    private val knotRepository: KnotRepository
) : UseCase<String, KnotVo>() {
    override suspend operator fun invoke(request: String): Flow<Response<KnotVo>> {
        return knotRepository.getKnot(request)
    }
}