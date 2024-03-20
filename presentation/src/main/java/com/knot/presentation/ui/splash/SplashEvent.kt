package com.knot.presentation.ui.splash

import com.knot.presentation.Event

sealed class SplashEvent : Event {
    object GoToMainEvent : SplashEvent()
    object GoToLoginEvent : SplashEvent()
}