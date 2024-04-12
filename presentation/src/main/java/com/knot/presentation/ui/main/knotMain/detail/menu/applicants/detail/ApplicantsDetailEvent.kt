package com.knot.presentation.ui.main.knotMain.detail.menu.applicants.detail

import com.knot.presentation.Event

sealed class ApplicantsDetailEvent : Event{
    object GoToBackEvent : ApplicantsDetailEvent()
}