package com.knot.presentation.ui.main.createOrEditKnot

import com.knot.presentation.Event

sealed class CreateOrEditKnotEvent : Event{
    object GoToBackEvent : CreateOrEditKnotEvent()
}