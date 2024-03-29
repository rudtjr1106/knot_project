package com.knot.presentation.ui.main.knotMain.detail.statistics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CalendarLayoutVo
import com.knot.domain.vo.TeamStatisticsVo
import com.knot.presentation.databinding.RecyclerItemAllStatisticsBinding
import com.knot.presentation.databinding.RecyclerLayoutKnotCalendarBinding
import com.knot.presentation.ui.main.knotMain.calendar.viewHolder.CalendarViewHolder
import com.knot.presentation.ui.main.knotMain.detail.statistics.viewHolder.StatisticsDetailViewHolder

class StatisticsDetailAdapter : ListAdapter<TeamStatisticsVo, RecyclerView.ViewHolder>(StatisticsDetailDiffCallBack()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StatisticsDetailViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemAllStatisticsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatisticsDetailViewHolder(binding)
    }
}

class StatisticsDetailDiffCallBack : DiffUtil.ItemCallback<TeamStatisticsVo>() {
    override fun areItemsTheSame(oldItem: TeamStatisticsVo, newItem: TeamStatisticsVo): Boolean = oldItem.type == newItem.type
    override fun areContentsTheSame(oldItem: TeamStatisticsVo, newItem: TeamStatisticsVo): Boolean = oldItem == newItem
}