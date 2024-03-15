package com.knot.domain.usecase.sign

import com.knot.domain.base.Response
import com.knot.domain.repository.SignRepository
import com.knot.domain.base.UseCase
import com.knot.domain.vo.UserInfoVo
import com.knot.domain.vo.response.KaKaoSignResponseVo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignKaKaoUseCase @Inject constructor(
    private val signRepository: SignRepository
) : UseCase<String, KaKaoSignResponseVo>() {
    override suspend operator fun invoke(request: String): Flow<Response<KaKaoSignResponseVo>> {
        return signRepository.kakaoLogin(request)
    }
}