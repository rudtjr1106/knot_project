package com.knot.presentation.ui.sign

import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SignViewModel @Inject constructor() : BaseViewModel<PageState.Default>() {
    override val uiState: PageState.Default
        get() = TODO("Not yet implemented")

}