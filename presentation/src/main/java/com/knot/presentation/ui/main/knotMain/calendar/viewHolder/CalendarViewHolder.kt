package com.knot.presentation.ui.main.knotMain.calendar.viewHolder

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.enums.CalendarDayViewType
import com.knot.domain.vo.CalendarDayVo
import com.knot.domain.vo.GatheringVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerLayoutKnotCalendarBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainTodoListAdapter
import com.knot.presentation.ui.main.knotMain.calendar.adapter.CalendarAdapter
import com.knot.presentation.ui.main.knotMain.calendar.adapter.CalendarDayAdapter
import com.knot.presentation.util.DateTimeFormatter
import com.knot.presentation.util.KnotLog

class CalendarViewHolder(
    private val binding: RecyclerLayoutKnotCalendarBinding,
    private val listener: CalendarAdapter.CalendarDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private val today = DateTimeFormatter.getToday()
    private val todayYear = DateTimeFormatter.getYear(today)
    private val todayMonth = DateTimeFormatter.getMonth(today)

    private val calendarDayAdapter : CalendarDayAdapter by lazy {
        CalendarDayAdapter(object : CalendarDayAdapter.CalendarDayDelegate{

        })
    }

    init {
        binding.apply {
            includeLayoutCalendar.recyclerViewCalendar.apply {
                layoutManager = GridLayoutManager(root.context, 7)
                adapter = calendarDayAdapter
            }
        }
    }

    fun bind(item : KnotVo) {
        binding.apply {
            textViewKnotTitle.text = item.title
            textViewYear.text = root.context.getString(R.string.main_calendar_year, todayYear)
            textViewMonth.text = todayMonth
        }
        calendarDayAdapter.submitList(getDayList(item))
    }

    private fun getDayList(knotVo: KnotVo) : List<CalendarDayVo> {
        val dayList = DateTimeFormatter.getMonthDays(todayYear.toInt(), todayMonth.toInt())
        val calendarDayList = mutableListOf<CalendarDayVo>()

        dayList.forEach {
            calendarDayList.add(
                CalendarDayVo(
                    type = CalendarDayViewType.VISIBLE,
                    isHoliday = DateTimeFormatter.isSunday(it),
                    isSaturday = DateTimeFormatter.isSaturday(it),
                    day = it,
                    isTodoExist = isTodoExist(knotVo.todoList, it),
                    isGatheringExist = isGatheringExist(knotVo.gatheringList, it)
                )
            )
        }

        return getEmptyStartDayList() + calendarDayList + getEmptyEndDayList()
    }

    private fun getEmptyStartDayList() : List<CalendarDayVo>{
        val startDay = DateTimeFormatter.getStartDayOfWeek(todayYear.toInt(), todayMonth.toInt())
        val list = mutableListOf<CalendarDayVo>()
        for (i in 0 until startDay){
            list.add(CalendarDayVo(type = CalendarDayViewType.GONE))
        }
        return list
    }

    private fun getEmptyEndDayList() : List<CalendarDayVo>{
        val endDay = DateTimeFormatter.getEndDayOfWeek(todayYear.toInt(), todayMonth.toInt())
        val list = mutableListOf<CalendarDayVo>()
        for (i in 0 until 6 - endDay){
            list.add(CalendarDayVo(type = CalendarDayViewType.GONE))
        }
        return list
    }

    private fun isTodoExist(todoMap : HashMap<String, TodoVo>, day : String) : Boolean{
        var isEqual = false
        todoMap.forEach {
            val dayList = DateTimeFormatter.getDatesBetween(it.value.startDay, it.value.endDay)
            dayList.forEach { todoDay ->
                if(todoDay == day) isEqual = true
            }
        }

        return isEqual
    }

    private fun isGatheringExist(gatheringMap : HashMap<String, GatheringVo>, day : String) : Boolean{
        var isEqual = false
        gatheringMap.forEach {
            if(it.value.gatheringDate == day) isEqual = true
        }

        return isEqual
    }
}