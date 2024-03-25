package com.knot.presentation.ui.main.knotMain.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CalendarLayoutVo
import com.knot.presentation.databinding.RecyclerLayoutKnotCalendarBinding
import com.knot.presentation.ui.main.knotMain.calendar.viewHolder.CalendarViewHolder

class CalendarAdapter(
    private val listener : CalendarDelegate
) : ListAdapter<CalendarLayoutVo, RecyclerView.ViewHolder>(CalendarDiffCallBack()) {

    interface CalendarDelegate {
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CalendarViewHolder -> holder.bind(currentList[position].knotVo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerLayoutKnotCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding, listener)
    }
}

class CalendarDiffCallBack : DiffUtil.ItemCallback<CalendarLayoutVo>() {
    override fun areItemsTheSame(oldItem: CalendarLayoutVo, newItem: CalendarLayoutVo): Boolean = oldItem.knotVo == newItem.knotVo
    override fun areContentsTheSame(oldItem: CalendarLayoutVo, newItem: CalendarLayoutVo): Boolean = oldItem == newItem
}