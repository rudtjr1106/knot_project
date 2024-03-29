package com.knot.presentation.ui.sign.signup

import com.knot.presentation.Event

sealed class SignUpEvent : Event {
    object GoToMainEvent : SignUpEvent()
    data class UpdateIntroTextLengthEvent(val length : String) : SignUpEvent()
}