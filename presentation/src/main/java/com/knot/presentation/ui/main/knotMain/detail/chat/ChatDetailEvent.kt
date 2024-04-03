package com.knot.presentation.ui.main.knotMain.detail.chat

import com.knot.presentation.Event

sealed class ChatDetailEvent : Event {
    object ScrollDownEvent : ChatDetailEvent()
}