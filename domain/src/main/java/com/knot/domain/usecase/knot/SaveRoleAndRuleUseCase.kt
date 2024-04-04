package com.knot.domain.usecase.knot

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.SaveRoleAndRuleRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveRoleAndRuleUseCase @Inject constructor(
    private val knotRepository: KnotRepository
) : UseCase<SaveRoleAndRuleRequest, Boolean>() {
    override suspend operator fun invoke(request: SaveRoleAndRuleRequest): Flow<Response<Boolean>> {
        return knotRepository.saveRoleAndRule(request)
    }
}