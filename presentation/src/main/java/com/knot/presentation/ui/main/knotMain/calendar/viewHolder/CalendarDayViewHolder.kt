package com.knot.presentation.ui.main.knotMain.calendar.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CalendarDayVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemCalendarDayBinding
import com.knot.presentation.ui.main.knotMain.calendar.adapter.CalendarDayAdapter
import com.knot.presentation.util.DateTimeFormatter

class CalendarDayViewHolder(
    private val binding: RecyclerItemCalendarDayBinding,
    private val listener: CalendarDayAdapter.CalendarDayDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private var calendarDayVo : CalendarDayVo = CalendarDayVo()

    init {
        binding.apply {
            constraintLayoutDay.setOnClickListener {
                if(calendarDayVo.day.isNotEmpty()) listener.onClickDay(calendarDayVo.day)
            }
        }
    }

    fun bind(item : CalendarDayVo) {
        val textColor = getTextColor(item)
        binding.apply {
            imageViewTodayCircle.visibility = if(item.day == DateTimeFormatter.getToday()) View.VISIBLE else View.INVISIBLE
            if(item.day.isEmpty()) textViewDayDate.text = ""
            else {
                textViewDayDate.setTextColor(root.context.getColor(textColor))
                calendarDayVo = item
                textViewDayDate.text = DateTimeFormatter.getDay(item.day).toInt().toString()
            }
            imageViewDayScheduleLine.visibility = if(item.isTodoExist) View.VISIBLE else View.INVISIBLE
            imageViewDayGatheringScheduleLine.visibility = if(item.isGatheringExist) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun getTextColor(item: CalendarDayVo) : Int{
        return if(item.isHoliday) R.color.color_e05744
               else if(item.isSaturday) R.color.color_1976d2
               else R.color.black
    }
}