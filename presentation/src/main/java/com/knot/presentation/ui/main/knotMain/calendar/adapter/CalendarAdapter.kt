package com.knot.presentation.ui.main.knotMain.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.enums.CalendarViewType
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
        return when(CalendarViewType.valueOf(viewType)){
            CalendarViewType.Calendar -> {
                val binding = RecyclerLayoutKnotCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CalendarViewHolder(binding, listener)
            }
            CalendarViewType.TODO -> {
                val binding = RecyclerLayoutKnotCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CalendarViewHolder(binding, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type.type
    }

}

class CalendarDiffCallBack : DiffUtil.ItemCallback<CalendarLayoutVo>() {
    override fun areItemsTheSame(oldItem: CalendarLayoutVo, newItem: CalendarLayoutVo): Boolean = oldItem.type == newItem.type
    override fun areContentsTheSame(oldItem: CalendarLayoutVo, newItem: CalendarLayoutVo): Boolean = oldItem == newItem
}