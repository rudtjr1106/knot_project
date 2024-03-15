package com.knot.domain.repository

import com.knot.domain.base.Response
import com.knot.domain.vo.UserInfoVo
import kotlinx.coroutines.flow.Flow

interface SignRepository {
    suspend fun kakaoLogin() : Flow<Response<UserInfoVo>>
}