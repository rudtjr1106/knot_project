package com.knot.domain.vo

data class ApplyKnotRequest(
    val knotId : String = "",
    val moreIntro : String = "",
    val userVo: UserVo = UserVo()
)
