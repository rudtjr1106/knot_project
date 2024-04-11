package com.knot.presentation.ui.main.knotMain.participatingKnot.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.KnotVo
import com.knot.presentation.databinding.RecyclerItemParticiaptingKnotListBinding
import com.knot.presentation.ui.main.knotMain.participatingKnot.adapter.KnotListAdapter
import com.knot.presentation.util.UserInfo

class KnotListViewHolder(
    private val binding: RecyclerItemParticiaptingKnotListBinding,
    private val listener: KnotListAdapter.KnotListDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var knotVo: KnotVo
    init {
        binding.apply {
            constraintLayoutKnot.setOnClickListener {
                if(knotVo.teamList.values.any { it.id == UserInfo.info.id }) listener.goToDetail(knotVo.knotId)
                else listener.goToDetail(knotVo.knotId)
            }
        }
    }

    fun bind(item : KnotVo) {
        knotVo = item
        binding.apply {
            textViewKnotTitle.text = item.title
            textViewKnotContent.text = item.content
            textViewParticipatePeople.text = item.teamList.size.toString()
        }

    }
}