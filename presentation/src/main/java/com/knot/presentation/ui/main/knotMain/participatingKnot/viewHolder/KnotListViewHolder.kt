package com.knot.presentation.ui.main.knotMain.participatingKnot.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.KnotVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemParticiaptingKnotListBinding
import com.knot.presentation.ui.main.knotMain.participatingKnot.adapter.KnotListAdapter

class KnotListViewHolder(
    private val binding: RecyclerItemParticiaptingKnotListBinding,
    private val listener: KnotListAdapter.KnotListDelegate
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {

        }
    }

    fun bind(item : KnotVo, position : String) {
        binding.apply {
            textViewKnotNumber.text = binding.root.context.getString(R.string.main_participating_knot_number, position)
            textViewKnotTitle.text = item.title
            textViewKnotContent.text = item.content
            textViewParticipatePeople.text = item.teamList.size.toString()
        }

    }
}