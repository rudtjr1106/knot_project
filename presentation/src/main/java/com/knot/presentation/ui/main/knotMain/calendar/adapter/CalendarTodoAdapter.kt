package com.knot.presentation.ui.main.knotMain.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CalendarTodoVo
import com.knot.presentation.databinding.RecyclerItemCalendarTodoBinding
import com.knot.presentation.ui.main.knotMain.calendar.viewHolder.CalendarTodoViewHolder

class CalendarTodoAdapter() : ListAdapter<CalendarTodoVo, RecyclerView.ViewHolder>(CalendarTodoDiffCallBack()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CalendarTodoViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemCalendarTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarTodoViewHolder(binding)
    }
}

class CalendarTodoDiffCallBack : DiffUtil.ItemCallback<CalendarTodoVo>() {
    override fun areItemsTheSame(oldItem: CalendarTodoVo, newItem: CalendarTodoVo): Boolean = oldItem.type == newItem.type
    override fun areContentsTheSame(oldItem: CalendarTodoVo, newItem: CalendarTodoVo): Boolean = oldItem == newItem
}