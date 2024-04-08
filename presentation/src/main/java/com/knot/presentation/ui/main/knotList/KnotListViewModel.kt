package com.knot.presentation.ui.main.knotList

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.GetKnotListUseCase
import com.knot.domain.vo.CategoryVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.SearchKnotRequest
import com.knot.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class KnotListViewModel @Inject constructor(
    private val getKnotListUseCase: GetKnotListUseCase
) : BaseViewModel<KnotListPageState>() {

    private val knotListStateFlow : MutableStateFlow<List<KnotVo>> = MutableStateFlow(
        emptyList()
    )
    private val categoryListStateFlow : MutableStateFlow<List<CategoryVo>> = MutableStateFlow(
        emptyList()
    )

    override val uiState: KnotListPageState = KnotListPageState(
        knotListStateFlow.asStateFlow(),
        categoryListStateFlow.asStateFlow()
    )

    fun getKnotList(request: SearchKnotRequest){
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
        }
    }

    fun updateEmptyCategoryList(list: List<String>){
        val categoryList = mutableListOf<CategoryVo>()
        list.forEach {
            categoryList.add(CategoryVo(category = it, isSelected = false))
        }
        updateCategoryList(categoryList.sortedByDescending { it.category })
    }

    private fun updateCategoryList(categoryList: List<CategoryVo>){
        viewModelScope.launch {
            categoryListStateFlow.update { categoryList }
        }
    }

    fun onClickCategory(categoryVo: CategoryVo){
        val newCategoryList = categoryListStateFlow.value.map {
            if(it.category == categoryVo.category) it.copy(isSelected = !categoryVo.isSelected)
            else it.copy(isSelected = false)
        }
        val request = if(!categoryVo.isSelected) SearchKnotRequest(category = categoryVo.category) else SearchKnotRequest()
        getKnotList(request)
        updateCategoryList(newCategoryList)
    }

}