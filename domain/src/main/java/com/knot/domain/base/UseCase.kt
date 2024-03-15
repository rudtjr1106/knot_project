package com.knot.domain.base

import kotlinx.coroutines.flow.Flow

abstract class UseCase<PARAM,Vo> {
    abstract suspend operator fun invoke(request:PARAM): Flow<Response<Vo>>
}