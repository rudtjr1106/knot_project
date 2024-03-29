package com.knot.presentation.ui.main.knotMain.detail.statistics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.enums.StatisticsDetailType
import com.knot.domain.vo.TeamStatisticsDetailVo
import com.knot.presentation.databinding.RecyclerItemNormalStatisticsBinding
import com.knot.presentation.databinding.RecyclerItemRankingStatisticsBinding
import com.knot.presentation.ui.main.knotMain.detail.statistics.viewHolder.StatisticsDetailNormalViewHolder
import com.knot.presentation.ui.main.knotMain.detail.statistics.viewHolder.StatisticsDetailRankingViewHolder

class StatisticsTeamAdapter: ListAdapter<TeamStatisticsDetailVo, RecyclerView.ViewHolder>(StatisticsTeamDiffCallBack()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StatisticsDetailNormalViewHolder -> holder.bind(currentList[position])
            is StatisticsDetailRankingViewHolder -> holder.bind(currentList[position], position + 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(StatisticsDetailType.valueOf(viewType)){
            StatisticsDetailType.TODO,
            StatisticsDetailType.GATHERING,
            StatisticsDetailType.CHAT -> {
                val binding = RecyclerItemNormalStatisticsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StatisticsDetailNormalViewHolder(binding)
            }
            StatisticsDetailType.ALL -> {
                val binding = RecyclerItemRankingStatisticsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StatisticsDetailRankingViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type.type
    }

}

class StatisticsTeamDiffCallBack : DiffUtil.ItemCallback<TeamStatisticsDetailVo>() {
    override fun areItemsTheSame(oldItem: TeamStatisticsDetailVo, newItem: TeamStatisticsDetailVo): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: TeamStatisticsDetailVo, newItem: TeamStatisticsDetailVo): Boolean = oldItem == newItem
}