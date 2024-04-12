package com.knot.presentation.ui.main.knotMain.detail.menu.applicants.detail

import com.knot.domain.vo.ApplicantUserVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.StateFlow

data class ApplicantsDetailPageState(
    val applicantUser: StateFlow<ApplicantUserVo>,
) : PageState