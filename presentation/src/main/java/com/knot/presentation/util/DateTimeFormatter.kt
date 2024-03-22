package com.knot.presentation.util

import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
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
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        for (i in 1..7) {
            dates.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dates
    }
    fun printMonthDetails(year: Int, month: Int) {
        // 해당 월의 첫날과 마지막날을 구함
        val startOfMonth = YearMonth.of(year, month).atDay(1)
        val endOfMonth = YearMonth.of(year, month).atEndOfMonth()

        // 시작 요일과 끝 요일을 구함
        val startDayOfWeek = startOfMonth.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val endDayOfWeek = endOfMonth.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

        println("Start Day of Week: $startDayOfWeek")
        println("End Day of Week: $endDayOfWeek")
        println("Dates of $month month, $year:")

        // 해당 월의 모든 날짜를 순회하며 출력
        var currentDate = startOfMonth
        while (currentDate.isBefore(endOfMonth) || currentDate.isEqual(endOfMonth)) {
            println(currentDate)
            currentDate = currentDate.plusDays(1)
        }
    }
}