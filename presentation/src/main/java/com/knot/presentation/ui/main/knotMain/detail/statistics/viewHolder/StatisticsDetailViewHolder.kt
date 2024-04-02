package com.knot.presentation.ui.main.knotMain.detail.statistics.viewHolder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.enums.StatisticsDetailType
import com.knot.domain.vo.TeamStatisticsVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemAllStatisticsBinding
import com.knot.presentation.ui.main.knotMain.detail.statistics.adapter.StatisticsTeamAdapter

class StatisticsDetailViewHolder(
    private val binding: RecyclerItemAllStatisticsBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val statisticsTeamAdapter : StatisticsTeamAdapter by lazy { StatisticsTeamAdapter() }

    init {
        binding.apply {
            recyclerViewStatistics.apply {
                layoutManager = LinearLayoutManager(root.context)
                adapter = statisticsTeamAdapter
            }
        }
    }

    fun bind(item : TeamStatisticsVo) {
        binding.apply {
            val text = when(item.type){
                StatisticsDetailType.TODO -> root.context.getString(R.string.main_knot_detail_todo_statistics)
                StatisticsDetailType.GATHERING -> root.context.getString(R.string.main_knot_detail_gathering_statistics)
                StatisticsDetailType.CHAT -> root.context.getString(R.string.main_knot_detail_chat_statistics)
                StatisticsDetailType.ALL -> root.context.getString(R.string.main_knot_detail_rank_statistics)
            }
            textViewStatisticsTitle.text = text
            statisticsTeamAdapter.submitList(item.teamStatisticsList)
        }
    }
}