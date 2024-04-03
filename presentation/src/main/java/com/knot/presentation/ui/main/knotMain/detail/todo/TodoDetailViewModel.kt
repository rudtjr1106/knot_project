package com.knot.presentation.ui.main.knotMain.detail.todo

import androidx.lifecycle.viewModelScope
import com.knot.domain.usecase.knot.CheckKnotTodoUseCase
import com.knot.domain.usecase.knot.GetKnotDetailUseCase
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TodoDetailVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.KnotLog
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        val todoDetailList = getTodoDetailList(result.todoList.values.toList())
        val sortedTodoDetailList = getFirstMyName(todoDetailList)
        viewModelScope.launch {
            todoListStateFlow.update { sortedTodoDetailList }
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

    private fun getFirstMyName(list: List<TodoDetailVo>): List<TodoDetailVo> {
        return list.sortedByDescending { it.todoList.any { todo -> todo.userId == UserInfo.info.id } }
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