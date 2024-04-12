package com.knot.presentation.ui.main.knotMain.detail.menu.applicants

import com.knot.presentation.Event

sealed class KnotApplicantsEvent : Event{
    object GoToBackEvent : KnotApplicantsEvent()
}