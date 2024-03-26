package com.knot.presentation.ui.main.knotMain.calendar

import com.knot.presentation.Event

sealed class CalendarEvent : Event{
    data class ShowBottomSheet(val list : List<String>) : CalendarEvent()
}