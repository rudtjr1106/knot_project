package com.knot.presentation.ui.main.knotMain.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.TeamStatisticsVo
import com.knot.presentation.databinding.RecyclerItemPersonalStatisticsPercentBinding
import com.knot.presentation.ui.main.knotMain.detail.viewHolder.TeamStatisticsViewHolder

class TeamStatisticsAdapter : ListAdapter<TeamStatisticsVo, RecyclerView.ViewHolder>(TeamStatisticsDiffCallBack()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TeamStatisticsViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemPersonalStatisticsPercentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamStatisticsViewHolder(binding)
    }
}

class TeamStatisticsDiffCallBack : DiffUtil.ItemCallback<TeamStatisticsVo>() {
    override fun areItemsTheSame(oldItem: TeamStatisticsVo, newItem: TeamStatisticsVo): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: TeamStatisticsVo, newItem: TeamStatisticsVo): Boolean = oldItem == newItem
}