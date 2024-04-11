package com.knot.presentation.ui.main.knotList.application.detail

import com.knot.presentation.Event

sealed class KnotApplicationEvent : Event{
    object GoToBackEvent : KnotApplicationEvent()
    data class GoToApplicationEvent(val knotId : String) : KnotApplicationEvent()
}