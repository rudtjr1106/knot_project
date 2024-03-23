package com.knot.presentation.ui.main.knotMain.calendar

import androidx.lifecycle.viewModelScope
import com.knot.domain.enums.CalendarViewType
import com.knot.domain.vo.CalendarLayoutVo
import com.knot.domain.vo.MainLayoutVo
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
import com.knot.presentation.ui.main.knotMain.MainPageState
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
        viewModelScope.launch {
            calendarLayoutStateFlow.update { calendarLayout }
        }
    }

    private fun getCalendar() : List<CalendarLayoutVo>{
        return listOf(
            CalendarLayoutVo(
                type = CalendarViewType.Calendar,
                knotVo = UserInfo.info.knotList["1번"]!!
            )
        )
    }

}