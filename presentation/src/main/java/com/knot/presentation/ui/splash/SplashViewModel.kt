package com.knot.presentation.ui.splash

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.sign.GetMyInfoUseCase
import com.knot.domain.usecase.sign.LoginUseCase
import com.knot.domain.vo.normal.UserVo
import com.knot.domain.vo.response.GetMyInfoResponse
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.KnotLog
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase
) : BaseViewModel<PageState.Default>() {
    override val uiState: PageState.Default
        get() = TODO("Not yet implemented")

    fun checkLogin(token : String){
        viewModelScope.launch {
            loginUseCase(token).collect{
                resultResponse(it, {successLogin()}, {goToLogin()})
            }
        }
    }

    private fun successLogin(){
        viewModelScope.launch {
            getMyInfoUseCase(Unit).collect{
                resultResponse(it, ::successGetMyInfo)
            }
        }
    }

    private fun successGetMyInfo(result : GetMyInfoResponse){
        val userVo = UserVo(
            email = result.email,
            id = result.id,
            intro = result.intro,
            major = result.major,
            name = result.name,
            organization = result.organization
        )
        UserInfo.updateInfo(userVo)
        goToMain()
    }

    private fun goToMain(){
        emitEventFlow(SplashEvent.GoToMainEvent)
    }

    private fun goToLogin(){
        emitEventFlow(SplashEvent.GoToLoginEvent)
    }
}