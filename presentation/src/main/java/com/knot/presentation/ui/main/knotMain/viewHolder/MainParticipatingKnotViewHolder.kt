package com.knot.presentation.ui.main.knotMain.viewHolder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.KnotVo
import com.knot.presentation.databinding.RecyclerLayoutMainParticipatingKnotBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainAdapter
import com.knot.presentation.ui.main.knotMain.adapter.MainParticipatingKnotAdapter

class MainParticipatingKnotViewHolder(
    private val binding: RecyclerLayoutMainParticipatingKnotBinding,
    private val listener: MainAdapter.MainDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private val mainParticipatingKnotAdapter : MainParticipatingKnotAdapter by lazy {
        MainParticipatingKnotAdapter(object : MainParticipatingKnotAdapter.MainParticipatingKnotDelegate{

        })
    }

    init {
        binding.apply {
            recyclerViewParticipatingKnot.apply {
                layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = mainParticipatingKnotAdapter
            }
        }
    }

    fun bind(item : List<KnotVo>) {
        mainParticipatingKnotAdapter.submitList(item)
    }
}