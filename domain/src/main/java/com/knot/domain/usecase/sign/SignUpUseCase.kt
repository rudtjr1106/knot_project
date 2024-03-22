package com.knot.domain.usecase.sign

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.SignRepository
import com.knot.domain.vo.UserVo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val signRepository: SignRepository
) : UseCase<UserVo, Boolean>() {
    override suspend operator fun invoke(request: UserVo): Flow<Response<Boolean>> {
        return signRepository.signUp(request)
    }
}