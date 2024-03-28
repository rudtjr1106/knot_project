package com.knot.presentation.util

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeFormatter {
    const val YEAR_INDEX = 0
    const val MONTH_INDEX = 1
    const val DAY_INDEX = 2
    fun getTodayDayOfWeek(): Int {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek - 1
    }

    fun getDayOfWeek(dateStr : String) : String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return when(calendar.get(Calendar.DAY_OF_WEEK)){
            Calendar.SUNDAY -> "일"
            Calendar.MONDAY -> "월"
            Calendar.TUESDAY -> "화"
            Calendar.WEDNESDAY -> "수"
            Calendar.THURSDAY -> "목"
            Calendar.FRIDAY -> "금"
            Calendar.SATURDAY -> "토"
            else -> ""
        }
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
        return "${dates[MONTH_INDEX].toInt()}월 ${dates[DAY_INDEX].toInt()}일"
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

    // 이번 주 날짜들 구하는거
    fun getWeekDates(): List<String> {
        val dates = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        for (i in 1..7) {
            dates.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dates
    }

    // 특정 년도와 월에 해당하는 날짜들을 구하기
    fun getMonthDays(year: Int, month: Int) : List<String>{
        val dayList = mutableListOf<String>()
        val startOfMonth = YearMonth.of(year, month).atDay(1)
        val endOfMonth = YearMonth.of(year, month).atEndOfMonth()

        var currentDate = startOfMonth
        while (currentDate.isBefore(endOfMonth) || currentDate.isEqual(endOfMonth)) {
            dayList.add(currentDate.toString())
            currentDate = currentDate.plusDays(1)
        }
        return dayList
    }

    // 특정 년도와 월에 해당하는 시작 요일 구하기 2024 3월 시작 요일
    fun getStartDayOfWeek(year: Int, month: Int) : Int {
        val startOfMonth = YearMonth.of(year, month).atDay(1)
        return (startOfMonth.dayOfWeek + 1).value - 1
    }

    // 특정 년도와 월에 해당하는 끝 요일 구하기 ex) 2024 3월 마지막 요일
    fun getEndDayOfWeek(year: Int, month: Int) : Int {
        val endOfMonth = YearMonth.of(year, month).atEndOfMonth()
        return (endOfMonth.dayOfWeek + 1).value - 1
    }

    fun getToday(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }

    fun isSunday(dateStr: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }

    fun isSaturday(dateStr: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
    }

    fun getNowTimeWithSecond() : String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("a h:mm:ss")
        return currentDateTime.format(formatter)
    }

    fun getNowTimeWithoutSecond() : String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("a h:mm")
        return currentDateTime.format(formatter)
    }
}