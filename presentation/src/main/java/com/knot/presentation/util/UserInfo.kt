package com.knot.presentation.util

import com.knot.domain.vo.UserVo

object UserInfo {

    var info: UserVo = UserVo()
        private set

    fun updateInfo(info: UserVo) {
        this.info = info
    }
}