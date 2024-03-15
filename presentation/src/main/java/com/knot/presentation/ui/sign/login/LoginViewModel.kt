package com.knot.presentation.ui.sign.login

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.sign.SignKaKaoUseCase
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.KnotLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signKaKaoUseCase: SignKaKaoUseCase
) : BaseViewModel<PageState.Default>() {
    override val uiState: PageState.Default
        get() = TODO("Not yet implemented")

    fun onClickKaKaoLogin(){
        emitEventFlow(LoginEvent.KaKaoLoginEvent)
    }

    fun signInKaKao(){
        viewModelScope.launch {
            KnotLog.D("테스트")
            signKaKaoUseCase(Unit).collect{
                KnotLog.D(it.toString())
            }
        }
    }
}