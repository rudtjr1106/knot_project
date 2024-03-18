package com.knot.presentation.util

import com.knot.domain.vo.normal.UserVo

object UserInfo {

    var info: UserVo = UserVo()
        private set

    fun updateInfo(info: UserVo) {
        this.info = info
    }

    const val APP_VERSION = "1.0.0"
}