package com.knot.presentation.ui.main.knotMain.calendar.viewHolder

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.view.View
import android.widget.DatePicker
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.enums.CalendarTodoType
import com.knot.domain.vo.CalendarDayVo
import com.knot.domain.vo.CalendarTodoVo
import com.knot.domain.vo.GatheringVo
import com.knot.domain.vo.KnotVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerLayoutKnotCalendarBinding
import com.knot.presentation.ui.main.knotMain.calendar.adapter.CalendarAdapter
import com.knot.presentation.ui.main.knotMain.calendar.adapter.CalendarDayAdapter
import com.knot.presentation.ui.main.knotMain.calendar.adapter.CalendarTodoAdapter
import com.knot.presentation.util.DateTimeFormatter
import com.knot.presentation.util.UserInfo
import org.threeten.bp.LocalDate

class CalendarViewHolder(
    private val binding: RecyclerLayoutKnotCalendarBinding,
    private val listener: CalendarAdapter.CalendarDelegate
) : RecyclerView.ViewHolder(binding.root) {

    companion object{
        const val ITEM_SPAN_COUNT = 7
    }

    private val today = DateTimeFormatter.getToday()
    private var year = DateTimeFormatter.getYear(today).toInt()
    private var month = DateTimeFormatter.getMonth(today).toInt()
    private var selectDay = today
    private val onDateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
        this.year = year
        this.month = month + 1
        val selectedDate = LocalDate.of(this.year, this.month, dayOfMonth)
        selectDay = selectedDate.format(org.threeten.bp.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        bind(knotVo)
    }

    private lateinit var knotVo: KnotVo

    private val calendarDayAdapter : CalendarDayAdapter by lazy {
        CalendarDayAdapter(object : CalendarDayAdapter.CalendarDayDelegate{
            override fun onClickDay(day: String) {
                if(DateTimeFormatter.getYear(day).toInt() == year && DateTimeFormatter.getMonth(day).toInt() == month){
                    setTodoView(day)
                }
            }
        })
    }

    private val calendarTodoAdapter : CalendarTodoAdapter by lazy { CalendarTodoAdapter() }

    init {
        binding.apply {
            imageButtonBack.setOnClickListener {
                listener.onClickBack()
            }
            textViewKnotTitle.setOnClickListener {
                listener.onClickKnotTitle()
            }
            imageButtonDownArrow.setOnClickListener {
                listener.onClickKnotTitle()
            }
            textViewYear.setOnClickListener {
                showDatePickerDialog()
            }
            textViewMonth.setOnClickListener {
                showDatePickerDialog()
            }

            includeLayoutCalendar.recyclerViewCalendar.apply {
                layoutManager = GridLayoutManager(root.context, ITEM_SPAN_COUNT)
                itemAnimator = null
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
            textViewYear.text = root.context.getString(R.string.main_calendar_year, year.toString())
            textViewMonth.text = root.context.getString(R.string.main_calendar_month, month.toString())
        }
        calendarDayAdapter.submitList(getDayList(item))
        setTodoView(selectDay)
    }

    private fun setTodoView(day : String){
        binding.apply {
            constraintLayoutTodayBox.visibility = if(day == today) View.VISIBLE else View.INVISIBLE
            textViewDay.text = root.context.getString(R.string.main_calendar_day_with_week, DateTimeFormatter.getDay(day).toInt(), DateTimeFormatter.getDayOfWeek(day))
            calendarTodoAdapter.submitList(getCalendarTodoList(day))
        }
    }

    private fun getDayList(knotVo: KnotVo) : List<CalendarDayVo> {
        val dayList = DateTimeFormatter.getMonthDays(year, month)
        val calendarDayList = mutableListOf<CalendarDayVo>()

        dayList.forEach {
            calendarDayList.add(
                CalendarDayVo(
                    isHoliday = DateTimeFormatter.isSunday(it),
                    isSaturday = DateTimeFormatter.isSaturday(it),
                    day = it,
                    isTodoExist = isTodoExist(knotVo.todoList, it),
                    isGatheringExist = isGatheringExist(knotVo.gatheringList, it)
                )
            )
        }
        return getEmptyStartDayList() + calendarDayList
    }

    private fun getEmptyStartDayList() : List<CalendarDayVo>{
        val startDay = DateTimeFormatter.getStartDayOfWeek(year, month)
        val list = mutableListOf<CalendarDayVo>()
        for (i in 0 until startDay){
            list.add(CalendarDayVo())
        }
        return list
    }

    private fun isTodoExist(todoMap : HashMap<String, TodoVo>, day : String) : Boolean{
        var isEqual = false
        todoMap.forEach {
            if(it.value.userId == UserInfo.info.id){
                val dayList = DateTimeFormatter.getDatesBetween(it.value.startDay, it.value.endDay)
                dayList.forEach { todoDay ->
                    if(todoDay == day) isEqual = true
                }
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
            if(todo.value.userId == UserInfo.info.id ){
                val betweenDay = DateTimeFormatter.getDatesBetween(todo.value.startDay, todo.value.endDay)
                betweenDay.forEach { dayItem ->
                    if(dayItem == day) todoList.add(
                        CalendarTodoVo(type = CalendarTodoType.TODO, todo = todo.value)
                    )
                }
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

    private fun showDatePickerDialog() {
        DatePickerDialog(binding.root.context, AlertDialog.THEME_HOLO_LIGHT, onDateSetListener,
            year, month -1, DateTimeFormatter.getDay(selectDay).toInt()).show()
    }
}