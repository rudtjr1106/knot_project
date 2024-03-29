package com.knot.presentation.ui.main.knotMain.detail.statistics.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.TeamStatisticsDetailVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemNormalStatisticsBinding

class StatisticsDetailNormalViewHolder(
    private val binding: RecyclerItemNormalStatisticsBinding,
) : RecyclerView.ViewHolder(binding.root) {

    init {
    }

    fun bind(item : TeamStatisticsDetailVo) {
        binding.apply {
            textViewName.text = item.name
            textViewPercent.text = root.context.getString(R.string.main_knot_detail_only_percent, item.statistics.toInt())
            progressParPercent.progress = item.statistics.toInt()
        }
    }
}