package com.knot.presentation.ui.main.knotMain.detail.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.TeamStatisticsDetailVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemPersonalStatisticsPercentBinding

class TeamStatisticsViewHolder(
    private val binding: RecyclerItemPersonalStatisticsPercentBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item : TeamStatisticsDetailVo) {
        binding.apply {
            textViewNameStatistics.text = root.context.getString(R.string.main_knot_detail_personal_percent, item.name, item.statistics)
        }
    }
}