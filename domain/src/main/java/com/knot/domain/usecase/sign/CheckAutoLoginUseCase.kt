package com.knot.domain.usecase.sign

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.SignRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckAutoLoginUseCase @Inject constructor(
    private val signRepository: SignRepository
) : UseCase<Unit, Boolean>() {
    override suspend operator fun invoke(request: Unit): Flow<Response<Boolean>> {
        return signRepository.checkAutoLogin()
    }
}