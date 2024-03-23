package com.knot.presentation.ui.main.knotMain.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.GatheringVo
import com.knot.domain.vo.MainLayoutVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.databinding.RecyclerLayoutMainTopBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainAdapter
import com.knot.presentation.util.DateTimeFormatter
import com.knot.presentation.util.DateTimeFormatter.getWeekDates
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class MainTopViewHolder(
    private val binding: RecyclerLayoutMainTopBinding,
    private val listener: MainAdapter.MainDelegate
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val SUNDAY = 0
        const val MONDAY = 1
        const val TUESDAY = 2
        const val WEDNESDAY = 3
        const val THURSDAY = 4
        const val FRIDAY = 5
        const val SATURDAY = 6
    }

    init {
        val dates = getWeekDates()
        binding.apply {
            includeLayoutWeek.textViewSundayDate.text = DateTimeFormatter.getDay(dates[SUNDAY])
            includeLayoutWeek.textViewMondayDate.text = DateTimeFormatter.getDay(dates[MONDAY])
            includeLayoutWeek.textViewTuesdayDate.text = DateTimeFormatter.getDay(dates[TUESDAY])
            includeLayoutWeek.textViewWednesdayDate.text = DateTimeFormatter.getDay(dates[WEDNESDAY])
            includeLayoutWeek.textViewThursdayDate.text = DateTimeFormatter.getDay(dates[THURSDAY])
            includeLayoutWeek.textViewFridayDate.text = DateTimeFormatter.getDay(dates[FRIDAY])
            includeLayoutWeek.textViewSaturdayDate.text = DateTimeFormatter.getDay(dates[SATURDAY])

            constraintLayoutWeek.setOnClickListener {
                listener.onClickWeek()
            }
        }
    }

    fun bind(itemTodo: List<TodoVo>, itemGathering: List<GatheringVo>) {
        showToday()
        itemTodo.forEach { showTodoLine(DateTimeFormatter.getDatesBetween(it.startDay, it.endDay)) }
        itemGathering.forEach { showGatheringLine(it.gatheringDate) }
    }

    private fun showToday() {
        binding.apply {
            when (DateTimeFormatter.getTodayDayOfWeek()) {
                SUNDAY -> includeLayoutWeek.imageViewSundayCircle.visibility = View.VISIBLE
                MONDAY -> includeLayoutWeek.imageViewMondayCircle.visibility = View.VISIBLE
                TUESDAY -> includeLayoutWeek.imageViewTuesdayCircle.visibility = View.VISIBLE
                WEDNESDAY -> includeLayoutWeek.imageViewWednesdayCircle.visibility = View.VISIBLE
                THURSDAY -> includeLayoutWeek.imageViewThursdayCircle.visibility = View.VISIBLE
                FRIDAY -> includeLayoutWeek.imageViewFridayCircle.visibility = View.VISIBLE
                SATURDAY -> includeLayoutWeek.imageViewSaturdayCircle.visibility = View.VISIBLE
            }
        }
    }

    private fun showTodoLine(list: List<String>) {
        val weekDate = getWeekDates()
        list.forEach {
            when (it) {
                weekDate[SUNDAY] -> binding.includeLayoutWeek.imageViewSundayScheduleLine.visibility = View.VISIBLE
                weekDate[MONDAY] -> binding.includeLayoutWeek.imageViewMondayScheduleLine.visibility = View.VISIBLE
                weekDate[TUESDAY] -> binding.includeLayoutWeek.imageViewTuesdayScheduleLine.visibility = View.VISIBLE
                weekDate[WEDNESDAY] -> binding.includeLayoutWeek.imageViewWednesdayScheduleLine.visibility = View.VISIBLE
                weekDate[THURSDAY] -> binding.includeLayoutWeek.imageViewThursdayScheduleLine.visibility = View.VISIBLE
                weekDate[FRIDAY] -> binding.includeLayoutWeek.imageViewFridayScheduleLine.visibility = View.VISIBLE
                weekDate[SATURDAY] -> binding.includeLayoutWeek.imageViewSaturdayScheduleLine.visibility = View.VISIBLE
            }
        }
    }

    private fun showGatheringLine(date: String) {
        val weekDate = getWeekDates()
        when (date) {
            weekDate[SUNDAY] -> binding.includeLayoutWeek.imageViewSundayGatheringScheduleLine.visibility = View.VISIBLE
            weekDate[MONDAY] -> binding.includeLayoutWeek.imageViewMondayGatheringScheduleLine.visibility = View.VISIBLE
            weekDate[TUESDAY] -> binding.includeLayoutWeek.imageViewTuesdayGatheringScheduleLine.visibility = View.VISIBLE
            weekDate[WEDNESDAY] -> binding.includeLayoutWeek.imageViewWednesdayGatheringScheduleLine.visibility = View.VISIBLE
            weekDate[THURSDAY] -> binding.includeLayoutWeek.imageViewThursdayGatheringScheduleLine.visibility = View.VISIBLE
            weekDate[FRIDAY] -> binding.includeLayoutWeek.imageViewFridayGatheringScheduleLine.visibility = View.VISIBLE
            weekDate[SATURDAY] -> binding.includeLayoutWeek.imageViewSaturdayGatheringScheduleLine.visibility = View.VISIBLE
        }
    }
}