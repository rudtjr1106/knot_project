package com.knot.presentation.ui.main.knotMain.calendar.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.enums.CalendarTodoType
import com.knot.domain.vo.CalendarTodoVo
import com.knot.domain.vo.GatheringVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemCalendarTodoBinding
import com.knot.presentation.util.DateTimeFormatter

class CalendarTodoViewHolder(
    private val binding: RecyclerItemCalendarTodoBinding,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.apply {

        }
    }

    fun bind(item : CalendarTodoVo) {
        when(item.type){
            CalendarTodoType.TODO -> setTodoView(item.todo)
            CalendarTodoType.GATHERING -> setGatheringView(item.gathering)
        }
    }

    private fun setTodoView(item : TodoVo){
        binding.apply {
            viewTodoGatheringLine.setBackgroundResource(R.drawable.bg_rectangle_filled_ffcc00_radius_4 )
            textViewTodoTitle.text = item.title
            textViewTodoContent.text = root.context.getString(R.string.main_todo_content,
                DateTimeFormatter.getMonthAndDayKor(item.startDay), DateTimeFormatter.getMonthAndDayKor(item.endDay), item.content)
        }
    }

    private fun setGatheringView(item : GatheringVo){
        binding.apply {
            viewTodoGatheringLine.setBackgroundResource(R.drawable.bg_rectangle_filled_e5e5e5_radius_4 )
            textViewTodoTitle.text = item.title
            textViewTodoContent.text = root.context.getString(R.string.main_gathering_content,
                DateTimeFormatter.getMonthAndDayKor(item.gatheringDate), item.content)
        }
    }
}