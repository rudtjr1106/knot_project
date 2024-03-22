package com.knot.presentation.ui.splash

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.sign.CheckAutoLoginUseCase
import com.knot.domain.usecase.sign.GetMyInfoUseCase
import com.knot.domain.vo.UserVo
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkAutoLoginUseCase: CheckAutoLoginUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase
) : BaseViewModel<PageState.Default>() {
    override val uiState: PageState.Default
        get() = TODO("Not yet implemented")

    fun checkLogin(){
        viewModelScope.launch {
            checkAutoLoginUseCase(Unit).collect{
                resultResponse(it, {goToMain()}, {goToLogin()})
            }
        }
    }

    private fun goToMain(){
        emitEventFlow(SplashEvent.GoToMainEvent)
    }

    private fun goToLogin(){
        emitEventFlow(SplashEvent.GoToLoginEvent)
    }
}