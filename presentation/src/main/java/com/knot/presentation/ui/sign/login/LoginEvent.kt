package com.knot.presentation.ui.sign.login

import com.knot.presentation.Event

sealed class LoginEvent : Event {
    object KaKaoLoginEvent : LoginEvent()
    data class SaveUserTokenEvent(val token : String) : LoginEvent()
    object GoToSignUpEvent : LoginEvent()
    object GoToMainEvent : LoginEvent()
}