package com.knot.presentation.ui.main.knotList

import com.knot.presentation.Event

sealed class KnotListEvent : Event{
    object GoToSearchEvent : KnotListEvent()
}