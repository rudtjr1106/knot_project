package com.knot.presentation.ui.main

import com.knot.presentation.Event

sealed class MainActivityEvent : Event{
    object GoToMain : MainActivityEvent()
    object GoToKnotList : MainActivityEvent()
    object GoToCreateKnot : MainActivityEvent()
    object GoToProfile : MainActivityEvent()
    object GoToSetting : MainActivityEvent()
}