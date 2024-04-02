package com.knot.presentation.ui.main.knotMain.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.KnotVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemMainParticipatingKnotBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainParticipatingKnotAdapter

class MainParticipatingKnotItemViewHolder(
    private val binding: RecyclerItemMainParticipatingKnotBinding,
    private val listener: MainParticipatingKnotAdapter.MainParticipatingKnotDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var knotVo: KnotVo
    init {
        binding.apply {
            constraintLayoutKnot.setOnClickListener {
                listener.onClickKnot(knotVo.knotId)
            }
        }
    }

    fun bind(item : KnotVo, position : String) {
        knotVo = item
        binding.apply {
            textViewKnotNumber.text = binding.root.context.getString(R.string.main_participating_knot_number, position)
            textViewKnotTitle.text = item.title
            textViewParticipatePeople.text = item.teamList.size.toString()
        }

    }
}