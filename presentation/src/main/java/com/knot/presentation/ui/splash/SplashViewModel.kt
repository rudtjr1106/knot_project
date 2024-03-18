package com.knot.presentation.ui.splash

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.sign.LoginUseCase
import com.knot.domain.vo.normal.UserVo
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<PageState.Default>() {
    override val uiState: PageState.Default
        get() = TODO("Not yet implemented")

    fun checkLogin(token : String){
        viewModelScope.launch {
            loginUseCase(token).collect{
                resultResponse(it, ::goToMain, ::goToLogin)
            }
        }
    }

    private fun goToMain(result : Boolean){
        emitEventFlow(SplashEvent.GoToMainEvent)
    }

    private fun goToLogin(error: Int){
        emitEventFlow(SplashEvent.GoToLoginEvent)
    }
}