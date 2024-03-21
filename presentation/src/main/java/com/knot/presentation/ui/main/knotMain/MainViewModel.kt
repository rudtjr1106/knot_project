package com.knot.presentation.ui.main.knotMain

import androidx.lifecycle.viewModelScope
import com.knot.domain.enums.MainVIewType
import com.knot.domain.vo.normal.MainLayoutVo
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseViewModel
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
class MainViewModel @Inject constructor() : BaseViewModel<MainPageState>() {

    private val mainLayoutStateFlow : MutableStateFlow<List<MainLayoutVo>> = MutableStateFlow(
        emptyList()
    )

    override val uiState: MainPageState = MainPageState(
        mainLayoutStateFlow.asStateFlow()
    )

    fun getData(){
        viewModelScope.launch {
            mainLayoutStateFlow.update { getWeekDates() + getTestKnot() + getTestTodo() }
        }
    }

    private fun getWeekDates(): List<MainLayoutVo> {
        val dates = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("dd")
        val calendar = Calendar.getInstance()

        // 오늘 날짜를 기준으로 설정
        calendar.time = Date()

        // 이번 주의 첫 번째 날(일요일)로 설정
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        // 일요일부터 토요일까지 날짜를 리스트에 추가
        for (i in 1..7) {
            dates.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return listOf(
            MainLayoutVo(
                type = MainVIewType.TOP,
                todoList = dates
        )
        )
    }

    private fun getTestKnot(): List<MainLayoutVo> {
        val dates = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("dd")
        val calendar = Calendar.getInstance()

        // 오늘 날짜를 기준으로 설정
        calendar.time = Date()

        // 이번 주의 첫 번째 날(일요일)로 설정
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        // 일요일부터 토요일까지 날짜를 리스트에 추가
        for (i in 1..7) {
            dates.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return listOf(
            MainLayoutVo(
                type = MainVIewType.PARTICIPATING_KNOT,
                todoList = dates
            )
        )
    }

    private fun getTestTodo(): List<MainLayoutVo> {
        val dates = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("dd")
        val calendar = Calendar.getInstance()

        // 오늘 날짜를 기준으로 설정
        calendar.time = Date()

        // 이번 주의 첫 번째 날(일요일)로 설정
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        // 일요일부터 토요일까지 날짜를 리스트에 추가
        for (i in 1..7) {
            dates.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return listOf(
            MainLayoutVo(
                type = MainVIewType.TODO_LIST,
                todoList = dates
            )
        )
    }
}