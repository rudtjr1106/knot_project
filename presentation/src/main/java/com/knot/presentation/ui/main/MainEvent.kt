package com.knot.presentation.ui.main

import com.knot.presentation.Event

sealed class MainEvent : Event{
    object GoToMain : MainEvent()
    object GoToKnotList : MainEvent()
    object GoToCreateKnot : MainEvent()
    object GoToProfile : MainEvent()
    object GoToSetting : MainEvent()
}