package com.knot.presentation.ui.main.knotMain.detail.menu.editRoleAndRule.viewHolder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.TeamRoleVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemKnotRoleBinding

class KnotRoleViewHolder(
    private val binding: RecyclerItemKnotRoleBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item : TeamRoleVo) {
        binding.apply {
            if(item.isHost){
                editTextRole.isEnabled = true
                textViewHostOrMember.text = root.context.getString(R.string.word_team_leader)
            }
            else {
                editTextRole.isEnabled = false
                textViewHostOrMember.text = root.context.getString(R.string.word_team_member)
                textViewHostOrMember.setTextColor(ContextCompat.getColor(root.context, R.color.black))
            }
            textViewId.text = item.teamUserVo.id
            textViewName.text = item.teamUserVo.name
            if(item.teamUserVo.role.isEmpty()){
                editTextRole.setText(root.context.getString(R.string.word_nothing))
            }
            else{
                editTextRole.setText(item.teamUserVo.role)
            }

        }
    }
}