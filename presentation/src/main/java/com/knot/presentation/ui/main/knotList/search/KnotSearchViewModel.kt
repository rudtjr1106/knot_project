package com.knot.presentation.ui.main.knotList.search

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.GetKnotListUseCase
import com.knot.domain.vo.CategoryVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.SearchKnotRequest
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.ui.main.knotList.KnotListPageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class KnotSearchViewModel @Inject constructor(
    private val getKnotListUseCase: GetKnotListUseCase
) : BaseViewModel<KnotSearchPageState>() {

    private val knotListStateFlow : MutableStateFlow<List<KnotVo>> = MutableStateFlow(
        emptyList()
    )
    private val searchContentStateFlow : MutableStateFlow<String> = MutableStateFlow("")
    private val isEmptyListStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val uiState: KnotSearchPageState = KnotSearchPageState(
        knotListStateFlow.asStateFlow(),
        searchContentStateFlow,
        isEmptyListStateFlow.asStateFlow()
    )

    fun getKnotList(){
        showLoading()
        val request = SearchKnotRequest(searchContent = searchContentStateFlow.value)
        viewModelScope.launch {
            getKnotListUseCase(request).collect{
                resultResponse(it, ::successGetKnotList)
            }
        }
    }

    private fun successGetKnotList(result : List<KnotVo>){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss")
        val sortedList = result.sortedByDescending { LocalDateTime.parse(it.createdAt, formatter) }
        updateKnotList(sortedList)
    }

    private fun updateKnotList(list : List<KnotVo>){
        viewModelScope.launch {
            knotListStateFlow.update { list }
            isEmptyListStateFlow.update { list.isEmpty() }
        }
    }

    fun onClickBack(){
        emitEventFlow(KnotSearchEvent.GoToBackEvent)
    }

}