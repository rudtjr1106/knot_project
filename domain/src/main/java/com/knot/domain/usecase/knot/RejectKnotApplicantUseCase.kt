package com.knot.domain.usecase.knot

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.RejectOrApproveTeamRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RejectKnotApplicantUseCase @Inject constructor(
    private val knotRepository: KnotRepository
) : UseCase<RejectOrApproveTeamRequest, Boolean>() {
    override suspend operator fun invoke(request: RejectOrApproveTeamRequest): Flow<Response<Boolean>> {
        return knotRepository.rejectKnotApplicant(request)
    }
}