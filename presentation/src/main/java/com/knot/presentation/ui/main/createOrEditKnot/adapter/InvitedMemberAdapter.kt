package com.knot.presentation.ui.main.createOrEditKnot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.TeamUserVo
import com.knot.presentation.databinding.RecyclerItemInvitedMemberBinding
import com.knot.presentation.ui.main.createOrEditKnot.viewHolder.InvitedMemberViewHolder

class InvitedMemberAdapter(
    private val listener : InvitedMemberDelegate
) : ListAdapter<TeamUserVo, RecyclerView.ViewHolder>(InvitedMemberDiffCallBack()) {

    interface InvitedMemberDelegate {
        fun onClickCancel(member : TeamUserVo)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is InvitedMemberViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemInvitedMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvitedMemberViewHolder(binding, listener)
    }
}

class InvitedMemberDiffCallBack : DiffUtil.ItemCallback<TeamUserVo>() {
    override fun areItemsTheSame(oldItem: TeamUserVo, newItem: TeamUserVo): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: TeamUserVo, newItem: TeamUserVo): Boolean = oldItem == newItem
}