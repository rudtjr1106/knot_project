package com.knot.presentation.ui.sign.login

import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.knot.domain.usecase.sign.SignKaKaoUseCase
import com.knot.domain.vo.normal.UserVo
import com.knot.domain.vo.response.KaKaoSignResponseVo
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.KnotLog
import com.knot.presentation.util.UserInfo
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
        updateUserInfo()
        if(data.isNewUser) emitEventFlow(LoginEvent.GoToSignUpEvent)
        else emitEventFlow(LoginEvent.GoToMainEvent)
    }

    private fun updateUserInfo(){
        UserApiClient.instance.me { user, error ->
            UserInfo.updateInfo(UserVo(
                name = user?.kakaoAccount?.profile?.nickname.toString(),
                email = user?.kakaoAccount?.email.toString()
            ))
        }
    }
}