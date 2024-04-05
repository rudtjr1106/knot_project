package com.knot.presentation.ui.main.knotMain.detail

import com.knot.presentation.Event

sealed class KnotDetailEvent : Event {
    object GoToBackEvent : KnotDetailEvent()
    object GoToStatisticsEvent : KnotDetailEvent()
    object GoToChatEvent : KnotDetailEvent()
    object GoToTodoEvent : KnotDetailEvent()
    object ShowBottomSheet : KnotDetailEvent()
    object GoToEditRuleRoleEvent : KnotDetailEvent()
    object GoToEditKnotEvent : KnotDetailEvent()
}