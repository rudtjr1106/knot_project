package com.knot.presentation.util

import java.util.Calendar

object DateTimeFormatter {
    fun getTodayDayOfWeek(): Int {
        val days = arrayOf("일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일")
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek - 1
    }
}