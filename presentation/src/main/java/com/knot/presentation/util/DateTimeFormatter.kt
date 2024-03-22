package com.knot.presentation.util

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DateTimeFormatter {
    const val YEAR_INDEX = 0
    const val MONTH_INDEX = 1
    const val DAY_INDEX = 2
    fun getTodayDayOfWeek(): Int {
        val days = arrayOf("일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일")
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek - 1
    }

    fun getYear(date : String) : String {
        val dates = date.split("-")
        return dates[YEAR_INDEX]
    }

    fun getMonth(date : String) : String {
        val dates = date.split("-")
        return dates[MONTH_INDEX]
    }

    fun getDay(date : String) : String {
        val dates = date.split("-")
        return dates[DAY_INDEX]
    }

    fun getMonthAndDayKor(date : String) : String {
        val dates = date.split("-")
        return "${dates[MONTH_INDEX]}월 ${dates[DAY_INDEX]}일"
    }

    fun getDatesBetween(startDateStr: String, endDateStr: String): List<String> {
        val dates = mutableListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val startDate = LocalDate.parse(startDateStr, formatter)
        val endDate = LocalDate.parse(endDateStr, formatter)

        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate.format(formatter))
            currentDate = currentDate.plusDays(1)
        }

        return dates
    }

    fun getWeekDates(): List<String> {
        val dates = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
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

        return dates
    }
}