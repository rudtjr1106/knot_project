package com.knot.presentation.ui.main.knotList.search

import com.knot.presentation.Event

sealed class KnotSearchEvent : Event{
    object GoToBackEvent : KnotSearchEvent()
}