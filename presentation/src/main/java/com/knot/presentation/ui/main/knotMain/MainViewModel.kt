package com.knot.presentation.ui.main.knotMain

import androidx.lifecycle.viewModelScope
import com.knot.domain.enums.MainVIewType
import com.knot.domain.usecase.sign.GetMyInfoUseCase
import com.knot.domain.vo.GatheringVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.MainLayoutVo
import com.knot.domain.vo.TodoVo
import com.knot.domain.vo.UserVo
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase
) : BaseViewModel<MainPageState>() {

    private val mainLayoutStateFlow : MutableStateFlow<List<MainLayoutVo>> = MutableStateFlow(
        emptyList()
    )

    override val uiState: MainPageState = MainPageState(
        mainLayoutStateFlow.asStateFlow()
    )
    fun getMyInfo(){
        viewModelScope.launch {
            getMyInfoUseCase(Unit).collect{
                resultResponse(it, ::successGetMyInfo)
            }
        }
    }

    private fun successGetMyInfo(result : UserVo){
        UserInfo.updateInfo(result)
        val todoList = getTodoList(result.knotList)
        val knotList = getKnotList(result.knotList)
        val gatheringList = getGatheringList(result.knotList)
        updateMainLayout(getTopView(todoList, gatheringList) + getParticipatingKnotView(knotList) + getTodoListView(todoList))
    }

    private fun getTodoList(knotMap : HashMap<String, KnotVo>) : List<TodoVo>{
        val todoList = mutableListOf<TodoVo>()
        knotMap.forEach{
            it.value.todoList.forEach { todo ->
                todoList.add(todo.value)
            }
        }
        return todoList
    }

    private fun getGatheringList(knotMap : HashMap<String, KnotVo>) : List<GatheringVo>{
        val gatheringList = mutableListOf<GatheringVo>()
        knotMap.forEach{
            it.value.gatheringList.forEach { gathering ->
                gatheringList.add(gathering.value)
            }
        }
        return gatheringList
    }


    private fun getKnotList(knotMap : HashMap<String, KnotVo>) : List<KnotVo>{
        val knotList = mutableListOf<KnotVo>()
        knotMap.forEach{
            knotList.add(it.value)
        }
        return knotList
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
}