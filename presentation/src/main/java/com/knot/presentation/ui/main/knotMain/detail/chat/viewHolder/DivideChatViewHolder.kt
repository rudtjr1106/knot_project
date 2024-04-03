package com.knot.presentation.ui.main.knotMain.detail.chat.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.presentation.databinding.RecyclerItemChatDivideLineBinding

class DivideChatViewHolder(
    private val binding: RecyclerItemChatDivideLineBinding,
) : RecyclerView.ViewHolder(binding.root) {

    init {
    }

    fun bind(item : String) {
        binding.apply {
            textViewDate.text = item
        }
    }
}