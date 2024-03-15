package com.knot.presentation.ui.sign.login

import com.knot.presentation.Event

sealed class LoginEvent : Event {
    object KaKaoLoginEvent : LoginEvent()
}