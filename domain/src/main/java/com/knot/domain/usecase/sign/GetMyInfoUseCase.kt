package com.knot.domain.usecase.sign

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.SignRepository
import com.knot.domain.vo.response.GetMyInfoResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyInfoUseCase @Inject constructor(
    private val signRepository: SignRepository
) : UseCase<Unit, GetMyInfoResponse>() {
    override suspend operator fun invoke(request: Unit): Flow<Response<GetMyInfoResponse>> {
        return signRepository.getMyInfo()
    }
}