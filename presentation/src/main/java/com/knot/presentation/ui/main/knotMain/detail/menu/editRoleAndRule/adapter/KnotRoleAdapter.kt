package com.knot.presentation.ui.main.knotMain.detail.menu.editRoleAndRule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.TeamRoleVo
import com.knot.presentation.databinding.RecyclerItemKnotRoleBinding
import com.knot.presentation.ui.main.knotMain.detail.menu.editRoleAndRule.viewHolder.KnotRoleViewHolder

class KnotRoleAdapter : ListAdapter<TeamRoleVo, RecyclerView.ViewHolder>(KnotRoleDiffCallBack()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is KnotRoleViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemKnotRoleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KnotRoleViewHolder(binding)
    }
}

class KnotRoleDiffCallBack : DiffUtil.ItemCallback<TeamRoleVo>() {
    override fun areItemsTheSame(oldItem: TeamRoleVo, newItem: TeamRoleVo): Boolean = oldItem.teamUserVo == newItem.teamUserVo
    override fun areContentsTheSame(oldItem: TeamRoleVo, newItem: TeamRoleVo): Boolean = oldItem == newItem
}