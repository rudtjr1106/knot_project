package com.knot.presentation.ui.main.knotList.application.apply

import com.knot.presentation.Event

sealed class KnotApplicationApplyEvent : Event{
    object GoToBackEvent : KnotApplicationApplyEvent()
}