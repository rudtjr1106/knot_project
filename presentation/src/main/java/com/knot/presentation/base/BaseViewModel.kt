package com.knot.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knot.domain.base.Response
import com.knot.domain.resultCode.ResultCode
import com.knot.presentation.PageState
import com.knot.presentation.Event
import com.knot.presentation.util.EventFlow
import com.knot.presentation.util.MutableEventFlow
import com.knot.presentation.util.asEventFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE: PageState> : ViewModel() {

    abstract val uiState:STATE

    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow: EventFlow<Event> = _eventFlow.asEventFlow()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    protected fun emitEventFlow(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    protected fun showLoading(){
        _isLoading.value = true
    }

    private fun endLoading(){
        _isLoading.value = false
    }

    protected fun<Vo> resultResponse(response: Response<Vo>, successCallback : (Vo) -> Unit, errorCallback : ((Int) -> Unit)? = null){
        if(response.result == ResultCode.SUCCESS) successCallback.invoke(response.data)
        else errorCallback?.invoke(response.result)
        endLoading()
    }
}