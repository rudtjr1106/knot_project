package com.knot.presentation.ui.main.knotMain.calendar

import androidx.lifecycle.viewModelScope
import com.knot.domain.vo.CalendarLayoutVo
import com.knot.domain.vo.KnotVo
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : BaseViewModel<CalendarPageState>() {

    private val calendarLayoutStateFlow : MutableStateFlow<List<CalendarLayoutVo>> = MutableStateFlow(
        emptyList()
    )

    override val uiState: CalendarPageState = CalendarPageState(
        calendarLayoutStateFlow.asStateFlow()
    )

    fun getData(){
        val calendarLayout = getCalendar()
        updateCalendar(calendarLayout)
    }

    private fun updateCalendar(list : List<CalendarLayoutVo>){
        viewModelScope.launch {
            calendarLayoutStateFlow.update { list }
        }
    }

    private fun getCalendar() : List<CalendarLayoutVo>{
        val knotVo = if(UserInfo.info.knotList.isNotEmpty()) UserInfo.info.knotList.entries.first().value
                    else KnotVo()
        return listOf(
            CalendarLayoutVo(
                knotVo = knotVo
            )
        )
    }

    fun showKnotListBottomSheet() {
        val list = mutableListOf<String>()
        UserInfo.info.knotList.forEach {
            list.add(it.value.title)
        }

        emitEventFlow(CalendarEvent.ShowBottomSheet(list))
    }

    fun onClickMenu(title : String){
        val calendarList = if(UserInfo.info.knotList.isNotEmpty()) getKnotData(title) else emptyList()
        updateCalendar(calendarList)
    }

    private fun getKnotData(title: String) : List<CalendarLayoutVo> {
        val list = mutableListOf<CalendarLayoutVo>()
        UserInfo.info.knotList.forEach {
            if(it.value.title == title) {
                list.add(CalendarLayoutVo(knotVo = it.value))
            }
        }
        return list
    }
}