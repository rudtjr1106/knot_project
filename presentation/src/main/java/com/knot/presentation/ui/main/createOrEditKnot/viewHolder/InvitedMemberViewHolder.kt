package com.knot.presentation.ui.main.createOrEditKnot.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.TeamUserVo
import com.knot.presentation.databinding.RecyclerItemInvitedMemberBinding
import com.knot.presentation.ui.main.createOrEditKnot.adapter.InvitedMemberAdapter

class InvitedMemberViewHolder(
    private val binding: RecyclerItemInvitedMemberBinding,
    private val listener: InvitedMemberAdapter.InvitedMemberDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var teamUserVo: TeamUserVo
    init {
        binding.apply {
            imageButtonCancel.setOnClickListener {
                listener.onClickCancel(teamUserVo)
            }
        }
    }

    fun bind(item : TeamUserVo) {
        teamUserVo = item
        binding.apply {
            textViewId.text = item.id
        }
    }
}