package com.knot.presentation.ui.main.knotMain

import androidx.lifecycle.viewModelScope
import com.knot.domain.enums.MainVIewType
import com.knot.domain.usecase.knot.CheckKnotTodoUseCase
import com.knot.domain.usecase.knot.GetMyKnotListUseCase
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.GatheringVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.MainLayoutVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMyKnotListUseCase: GetMyKnotListUseCase,
    private val checkKnotTodoUseCase: CheckKnotTodoUseCase,
) : BaseViewModel<MainPageState>() {

    private val mainLayoutStateFlow : MutableStateFlow<List<MainLayoutVo>> = MutableStateFlow(
        emptyList()
    )

    override val uiState: MainPageState = MainPageState(
        mainLayoutStateFlow.asStateFlow()
    )
    fun getMyKnotList(){
        showLoading()
        viewModelScope.launch {
            getMyKnotListUseCase(Unit).collect{
                resultResponse(it, ::successGetMyInfo)
            }
        }
    }

    private fun successGetMyInfo(result : List<KnotVo>){
        val todoList = getTodoList(result)
        val gatheringList = getGatheringList(result)
        updateMainLayout(getTopView(todoList, gatheringList) + getParticipatingKnotView(result) + getTodoListView(todoList))
    }

    private fun getTodoList(knotList : List<KnotVo>) : List<TodoVo>{
        val todoList = mutableListOf<TodoVo>()
        knotList.forEach{
            it.todoList.forEach { todo ->
                todoList.add(todo.value)
            }
        }
        return getSortedTodoList(todoList)
    }

    private fun getSortedTodoList(todoList : List<TodoVo>) : List<TodoVo>{
        return todoList.sortedWith(compareBy<TodoVo> { it.complete }.thenBy { LocalDate.parse(it.startDay) })
    }

    private fun getGatheringList(knotList : List<KnotVo>) : List<GatheringVo>{
        val gatheringList = mutableListOf<GatheringVo>()
        knotList.forEach{
            it.gatheringList.forEach { gathering ->
                gatheringList.add(gathering.value)
            }
        }
        return gatheringList
    }

    private fun getTopView(todoList : List<TodoVo>, gatheringList : List<GatheringVo>) : List<MainLayoutVo>{
        return listOf(
            MainLayoutVo(
                type = MainVIewType.TOP,
                todoList = todoList,
                gatheringList = gatheringList
            )
        )
    }

    private fun getParticipatingKnotView(list : List<KnotVo>) : List<MainLayoutVo>{
        return listOf(
            MainLayoutVo(
                type = MainVIewType.PARTICIPATING_KNOT,
                participatingKnotList = list
            )
        )
    }

    private fun getTodoListView(list: List<TodoVo>) : List<MainLayoutVo>{
        return listOf(
            MainLayoutVo(
                type = MainVIewType.TODO_LIST,
                todoList = list
            )
        )
    }

    private fun updateMainLayout(list : List<MainLayoutVo>){
        viewModelScope.launch {
            mainLayoutStateFlow.update { list }
        }
    }

    fun onClickComplete(request: CheckKnotTodoRequest){
        viewModelScope.launch {
            checkKnotTodoUseCase(request).collect{
                getMyKnotList()
            }
        }
    }
}