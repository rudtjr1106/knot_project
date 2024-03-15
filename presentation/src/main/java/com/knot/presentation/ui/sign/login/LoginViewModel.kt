package com.knot.presentation.ui.sign.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.sign.SignKaKaoUseCase
import com.knot.domain.vo.UserInfoVo
import com.knot.domain.vo.response.KaKaoSignResponseVo
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.KnotLog
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun signInKaKao(accessToken : String){
        viewModelScope.launch {
            signKaKaoUseCase(accessToken).collect{
                resultResponse(it, ::handleSuccess)
            }
        }
    }

    private fun handleSuccess(data : KaKaoSignResponseVo){
        KnotLog.D(data.uid)
        if(data.isNewUser) emitEventFlow(LoginEvent.GoToSignUpEvent)
        else emitEventFlow(LoginEvent.GoToMainEvent)
    }
}