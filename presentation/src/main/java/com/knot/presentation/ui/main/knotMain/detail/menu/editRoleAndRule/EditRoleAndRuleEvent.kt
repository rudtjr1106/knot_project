package com.knot.presentation.ui.main.knotMain.detail.menu.editRoleAndRule

import com.knot.presentation.Event

sealed class EditRoleAndRuleEvent : Event{
    object GoToBack : EditRoleAndRuleEvent()
    object OnClickEditRule : EditRoleAndRuleEvent()
}