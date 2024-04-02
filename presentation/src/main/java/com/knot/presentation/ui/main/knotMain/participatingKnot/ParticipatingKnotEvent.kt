package com.knot.presentation.ui.main.knotMain.participatingKnot

import com.knot.presentation.Event

sealed class ParticipatingKnotEvent : Event {
    object GoToBack : ParticipatingKnotEvent()
}