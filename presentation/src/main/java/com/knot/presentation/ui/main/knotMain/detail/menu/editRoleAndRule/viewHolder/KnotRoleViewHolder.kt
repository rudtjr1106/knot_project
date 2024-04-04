package com.knot.presentation.ui.main.knotMain.detail.menu.editRoleAndRule.viewHolder

import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.TeamRoleVo
import com.knot.domain.vo.TeamUserVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemKnotRoleBinding
import com.knot.presentation.ui.main.knotMain.detail.menu.editRoleAndRule.adapter.KnotRoleAdapter
import com.knot.presentation.util.UserInfo

class KnotRoleViewHolder(
    private val binding: RecyclerItemKnotRoleBinding,
    private val listener : KnotRoleAdapter.KnotRoleDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var teamUserVo: TeamUserVo

    init {
        binding.apply {
            editTextRole.doAfterTextChanged { role ->
                listener.onChangedRole(teamUserVo.copy(role = role.toString()))
            }
        }
    }

    fun bind(item : TeamRoleVo) {
        teamUserVo = item.teamUserVo
        binding.apply {
            if(item.isHost){
                textViewHostOrMember.text = root.context.getString(R.string.word_team_leader)
                textViewHostOrMember.setTextColor(ContextCompat.getColor(root.context, R.color.color_ffcc00))
            }
            else {
                textViewHostOrMember.text = root.context.getString(R.string.word_team_member)
                textViewHostOrMember.setTextColor(ContextCompat.getColor(root.context, R.color.black))
            }
            editTextRole.isEnabled = item.leader == UserInfo.info.id
            textViewId.text = item.teamUserVo.id
            textViewName.text = item.teamUserVo.name
            if(item.teamUserVo.role.isEmpty()) editTextRole.setText(root.context.getString(R.string.word_nothing))
            else editTextRole.setText(item.teamUserVo.role)
        }
    }
}