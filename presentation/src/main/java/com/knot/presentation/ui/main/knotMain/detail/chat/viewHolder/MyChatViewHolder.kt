package com.knot.presentation.ui.main.knotMain.detail.chat.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.ChatVo
import com.knot.presentation.databinding.RecyclerItemMyChatBinding

class MyChatViewHolder(
    private val binding: RecyclerItemMyChatBinding,
) : RecyclerView.ViewHolder(binding.root) {

    init {
    }

    fun bind(item : ChatVo) {
        binding.apply {
            textViewMyChat.text = item.content
            val readCount = getReadCount(item.readWho.values.toList())
            textViewReadCount.text = readCount.toString()
            if(readCount == 0) textViewReadCount.visibility = View.GONE
            else textViewReadCount.visibility = View.VISIBLE
            textViewChatTime.text = item.time
        }
    }

    private fun getReadCount(list: List<Boolean>) : Int{
        var trueCount = 0
        list.forEach { if(it) trueCount++ }
        return (list.size - trueCount)
    }
}