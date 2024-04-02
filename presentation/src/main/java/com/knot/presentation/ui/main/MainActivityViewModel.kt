package com.knot.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.knot.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : BaseViewModel<MainActivityPageState>() {

    private val isMainPageStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val isKnotListPageStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val isProfilePageStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val isSettingPageStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val isOtherPageStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val uiState: MainActivityPageState = MainActivityPageState(
        isMainPageStateFlow.asStateFlow(),
        isKnotListPageStateFlow.asStateFlow(),
        isProfilePageStateFlow.asStateFlow(),
        isSettingPageStateFlow.asStateFlow(),
        isOtherPageStateFlow.asStateFlow()
    )

    fun onClickMain() {
        activeMain()
        emitEventFlow(MainActivityEvent.GoToMain)
    }

    fun onClickKnotList() {
        activeKnotList()
        emitEventFlow(MainActivityEvent.GoToKnotList)
    }

    fun onClickCreateKnot() {
        goneNavigation()
        emitEventFlow(MainActivityEvent.GoToCreateKnot)
    }

    fun onClickProfile() {
        activeProfile()
        emitEventFlow(MainActivityEvent.GoToProfile)
    }

    fun onClickSetting() {
        activieSetting()
        emitEventFlow(MainActivityEvent.GoToSetting)
    }

    fun activeMain(){
        viewModelScope.launch {
            isMainPageStateFlow.update { true }
            isKnotListPageStateFlow.update { false }
            isProfilePageStateFlow.update { false }
            isSettingPageStateFlow.update { false }
            isOtherPageStateFlow.update { false }
        }
    }

    fun activeKnotList(){
        viewModelScope.launch {
            isMainPageStateFlow.update { false }
            isKnotListPageStateFlow.update { true }
            isProfilePageStateFlow.update { false }
            isSettingPageStateFlow.update { false }
            isOtherPageStateFlow.update { false }
        }
    }

    fun activeProfile(){
        viewModelScope.launch {
            isMainPageStateFlow.update { false }
            isKnotListPageStateFlow.update { false }
            isProfilePageStateFlow.update { true }
            isSettingPageStateFlow.update { false }
            isOtherPageStateFlow.update { false }
        }
    }

    fun activieSetting(){
        viewModelScope.launch {
            isMainPageStateFlow.update { false }
            isKnotListPageStateFlow.update { false }
            isProfilePageStateFlow.update { false }
            isSettingPageStateFlow.update { true }
            isOtherPageStateFlow.update { false }
        }
    }

    fun goneNavigation(){
        viewModelScope.launch {
            isOtherPageStateFlow.update { true }
        }
    }
}