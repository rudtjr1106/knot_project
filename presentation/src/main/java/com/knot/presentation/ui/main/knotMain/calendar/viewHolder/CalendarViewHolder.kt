package com.knot.presentation.ui.main.knotMain.calendar.viewHolder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.enums.CalendarDayViewType
import com.knot.domain.enums.CalendarTodoType
import com.knot.domain.vo.CalendarDayVo
import com.knot.domain.vo.CalendarTodoVo
import com.knot.domain.vo.GatheringVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerLayoutKnotCalendarBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainTodoListAdapter
import com.knot.presentation.ui.main.knotMain.calendar.adapter.CalendarAdapter
import com.knot.presentation.ui.main.knotMain.calendar.adapter.CalendarDayAdapter
import com.knot.presentation.ui.main.knotMain.calendar.adapter.CalendarTodoAdapter
import com.knot.presentation.util.DateTimeFormatter
import com.knot.presentation.util.KnotLog

class CalendarViewHolder(
    private val binding: RecyclerLayoutKnotCalendarBinding,
    private val listener: CalendarAdapter.CalendarDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private val today = DateTimeFormatter.getToday()
    private val todayYear = DateTimeFormatter.getYear(today)
    private val todayMonth = DateTimeFormatter.getMonth(today)
    private lateinit var knotVo: KnotVo

    private val calendarDayAdapter : CalendarDayAdapter by lazy {
        CalendarDayAdapter(object : CalendarDayAdapter.CalendarDayDelegate{
            override fun onClickDay(day: String) {
                setTodoView(day)
            }
        })
    }

    private val calendarTodoAdapter : CalendarTodoAdapter by lazy { CalendarTodoAdapter() }

    init {
        binding.apply {
            includeLayoutCalendar.recyclerViewCalendar.apply {
                layoutManager = GridLayoutManager(root.context, 7)
                adapter = calendarDayAdapter
            }

            recyclerViewTodo.apply {
                layoutManager = LinearLayoutManager(root.context)
                itemAnimator = null
                adapter = calendarTodoAdapter
            }
        }
    }

    fun bind(item : KnotVo) {
        knotVo = item
        binding.apply {
            textViewKnotTitle.text = item.title
            textViewYear.text = root.context.getString(R.string.main_calendar_year, todayYear)
            textViewMonth.text = root.context.getString(R.string.main_calendar_month, todayMonth.toInt().toString())
        }
        calendarDayAdapter.submitList(getDayList(item))
        setTodoView(today)
    }

    private fun setTodoView(day : String){
        binding.apply {
            constraintLayoutTodayBox.visibility = if(day == today) View.VISIBLE else View.INVISIBLE
            textViewDay.text = root.context.getString(R.string.main_calendar_day_with_week, DateTimeFormatter.getDay(day).toInt(), DateTimeFormatter.getDayOfWeek(day))
            calendarTodoAdapter.submitList(getCalendarTodoList(day))
        }
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

    private fun getCalendarTodoList(day : String) : List<CalendarTodoVo>{
        return getTodoList(day) + getGatheringList(day)
    }

    private fun getTodoList(day: String) : List<CalendarTodoVo>{
        val todoList = mutableListOf<CalendarTodoVo>()
        knotVo.todoList.forEach { todo ->
            val betweenDay = DateTimeFormatter.getDatesBetween(todo.value.startDay, todo.value.endDay)
            betweenDay.forEach { dayItem ->
                if(dayItem == day) todoList.add(
                    CalendarTodoVo(type = CalendarTodoType.TODO, todo = todo.value)
                )
            }
        }

        return todoList
    }

    private fun getGatheringList(day: String) : List<CalendarTodoVo>{
        val gatheringList = mutableListOf<CalendarTodoVo>()
        knotVo.gatheringList.forEach {
            if(it.value.gatheringDate == day) gatheringList.add(
                CalendarTodoVo(type = CalendarTodoType.GATHERING, gathering = it.value)
            )
        }

        return gatheringList
    }
}