package com.knot.domain.usecase.knot

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.SearchKnotRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetKnotListUseCase @Inject constructor(
    private val knotRepository: KnotRepository
) : UseCase<SearchKnotRequest, List<KnotVo>>() {
    override suspend operator fun invoke(request: SearchKnotRequest): Flow<Response<List<KnotVo>>> {
        return knotRepository.getKnotList(request)
    }
}