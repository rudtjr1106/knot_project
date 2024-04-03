package com.knot.presentation.ui.main.knotMain.detail.todo

import com.knot.presentation.Event

sealed class TodoDetailEvent : Event{
    object GoToBackEvent : TodoDetailEvent()
}