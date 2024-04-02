package com.knot.domain.usecase.knot

import com.knot.domain.base.Response
import com.knot.domain.base.UseCase
import com.knot.domain.repository.KnotRepository
import com.knot.domain.vo.CheckKnotTodoRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckKnotTodoUseCase @Inject constructor(
    private val knotRepository: KnotRepository
) : UseCase<CheckKnotTodoRequest, Boolean>() {
    override suspend operator fun invoke(request: CheckKnotTodoRequest): Flow<Response<Boolean>> {
        return knotRepository.checkKnotTodo(request)
    }
}