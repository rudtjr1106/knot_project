package com.knot.presentation.ui.sign.signup

import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.knot.domain.usecase.sign.SignUpUseCase
import com.knot.domain.vo.request.SignUpRequest
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.KnotLog
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel<SignUpPageState>() {

    private val idStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val organizationStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val majorStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val introduceStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val enableButtonStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val uiState: SignUpPageState = SignUpPageState(
        idStateFlow,
        organizationStateFlow,
        majorStateFlow,
        introduceStateFlow,
        enableButtonStateFlow.asStateFlow()
    )

    init {
        onChangedIntroTextAfter()
    }

    fun onChangedTextAfter(){
        val isNotEmptyText = idStateFlow.value.isNotEmpty() && organizationStateFlow.value.isNotEmpty() &&
                majorStateFlow.value.isNotEmpty()
        viewModelScope.launch {
            enableButtonStateFlow.update { isNotEmptyText }
        }
    }

    fun onChangedIntroTextAfter(){
        viewModelScope.launch {
            emitEventFlow(SignUpEvent.UpdateIntroTextLengthEvent(introduceStateFlow.value.length.toString()))
        }
    }

    fun onClickStartButton(){
        val request = getSignUpRequest()
        viewModelScope.launch {
            signUpUseCase(request).collect {
                resultResponse(it, {handleSuccessSignUp(request)})
            }
        }
    }

    private fun getSignUpRequest() : SignUpRequest {
        return SignUpRequest(
            email = UserInfo.info.email,
            id = idStateFlow.value,
            intro = introduceStateFlow.value,
            major = majorStateFlow.value,
            name = UserInfo.info.name,
            organization = organizationStateFlow.value
        )
    }

    private fun handleSuccessSignUp(result : SignUpRequest){
        UserInfo.apply {
            updateInfo(info.copy(
                id = result.id,
                intro = result.intro,
                major = result.major,
                organization = result.organization
            ))
        }
        emitEventFlow(SignUpEvent.GoToMainEvent)
    }
}