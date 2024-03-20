package com.knot.presentation.ui.sign.signup

import com.knot.presentation.PageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SignUpPageState(
    var id : MutableStateFlow<String>,
    var organization : MutableStateFlow<String>,
    var major : MutableStateFlow<String>,
    var introduce : MutableStateFlow<String>,
    val enableButton : StateFlow<Boolean>
) : PageState