package com.knot.presentation.ui.main.knotMain.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CalendarDayVo
import com.knot.presentation.databinding.RecyclerItemCalendarDayBinding
import com.knot.presentation.ui.main.knotMain.calendar.viewHolder.CalendarDayViewHolder

class CalendarDayAdapter(
    private val listener : CalendarDayDelegate
) : ListAdapter<CalendarDayVo, RecyclerView.ViewHolder>(CalendarDayDiffCallBack()) {

    interface CalendarDayDelegate {
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CalendarDayViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarDayViewHolder(binding, listener)
    }
}

class CalendarDayDiffCallBack : DiffUtil.ItemCallback<CalendarDayVo>() {
    override fun areItemsTheSame(oldItem: CalendarDayVo, newItem: CalendarDayVo): Boolean = oldItem.day == newItem.day
    override fun areContentsTheSame(oldItem: CalendarDayVo, newItem: CalendarDayVo): Boolean = oldItem == newItem
}