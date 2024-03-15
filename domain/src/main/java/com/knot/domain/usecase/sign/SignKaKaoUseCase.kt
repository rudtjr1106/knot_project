package com.knot.domain.usecase.sign

import com.knot.domain.base.Response
import com.knot.domain.repository.SignRepository
import com.knot.domain.base.UseCase
import com.knot.domain.vo.UserInfoVo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignKaKaoUseCase @Inject constructor(
    private val signRepository: SignRepository
) : UseCase<Unit, UserInfoVo>() {
    override suspend operator fun invoke(request: Unit): Flow<Response<UserInfoVo>> {
        return signRepository.kakaoLogin()
    }
}