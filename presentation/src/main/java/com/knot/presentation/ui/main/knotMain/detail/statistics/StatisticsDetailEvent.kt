package com.knot.presentation.ui.main.knotMain.detail.statistics

import com.knot.presentation.Event

sealed class StatisticsDetailEvent : Event{
    object GoToBackEvent : StatisticsDetailEvent()
}