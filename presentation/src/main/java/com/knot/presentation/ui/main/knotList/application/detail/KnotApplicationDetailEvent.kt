package com.knot.presentation.ui.main.knotList.application.detail

import com.knot.presentation.Event

sealed class KnotApplicationDetailEvent : Event{
    object GoToBackEvent : KnotApplicationDetailEvent()
    data class GoToApplicationEvent(val knotId : String) : KnotApplicationDetailEvent()
}