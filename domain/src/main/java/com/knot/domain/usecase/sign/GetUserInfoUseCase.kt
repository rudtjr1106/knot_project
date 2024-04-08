package com.knot.domain.usecase.sign

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.SignRepository
import com.knot.domain.vo.UserVo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val signRepository: SignRepository
) : UseCase<String, UserVo>() {
    override suspend operator fun invoke(request: String): Flow<Response<UserVo>> {
        return signRepository.getUserInfo(request)
    }
}