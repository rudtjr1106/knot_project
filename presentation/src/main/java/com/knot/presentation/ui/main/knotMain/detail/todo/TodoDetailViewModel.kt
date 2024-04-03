package com.knot.presentation.ui.main.knotMain.detail.todo

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.CheckKnotTodoUseCase
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TodoDetailVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.KnotLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val getKnotDetailUseCase: GetKnotDetailUseCase,
    private val checkKnotTodoUseCase: CheckKnotTodoUseCase
) : BaseViewModel<TodoDetailPageState>() {

    private val todoListStateFlow : MutableStateFlow<List<TodoDetailVo>> = MutableStateFlow(
        emptyList()
    )

    override val uiState: TodoDetailPageState = TodoDetailPageState(
        todoListStateFlow.asStateFlow()
    )

    fun getKnotDetail(knotId : String){
        viewModelScope.launch {
            getKnotDetailUseCase(knotId).collect{
                resultResponse(it, ::successGetKnotDetail)
            }
        }
    }

    private fun successGetKnotDetail(result : KnotVo){
        val tooDetailList = getTodoDetailList(result.todoList.values.toList())
        viewModelScope.launch {
            todoListStateFlow.update { tooDetailList }
        }
    }

    private fun getTodoDetailList(list: List<TodoVo>) : List<TodoDetailVo>{
        val groupTodo = list.groupBy { it.userId }
        val todoDetailList = groupTodo.map { (userId, todoList) ->
            TodoDetailVo(getSortedTodoList(todoList))
        }
        KnotLog.D(todoDetailList.toString())
        return todoDetailList
    }

    private fun getSortedTodoList(todoList : List<TodoVo>) : List<TodoVo>{
        return todoList.sortedWith(compareBy<TodoVo> { it.complete }.thenBy { LocalDate.parse(it.startDay) })
    }

    fun onClickComplete(request: CheckKnotTodoRequest){
        showLoading()
        viewModelScope.launch {
            checkKnotTodoUseCase(request).collect{
                resultResponse(it, {getKnotDetail(request.knotId)})
            }
        }
    }

    fun onClickBack(){
        emitEventFlow(TodoDetailEvent.GoToBackEvent)
    }
}