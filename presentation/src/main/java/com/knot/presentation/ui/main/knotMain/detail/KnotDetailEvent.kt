package com.knot.presentation.ui.main.knotMain.detail

import com.knot.presentation.Event

sealed class KnotDetailEvent : Event {
    object GoToBack : KnotDetailEvent()
}