package com.knot.presentation.ui.main.knotMain.detail.chat.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.enums.ChatType
import com.knot.domain.vo.ChatVo
import com.knot.presentation.databinding.RecyclerItemOtherChatBinding

class OtherChatViewHolder(
    private val binding: RecyclerItemOtherChatBinding,
) : RecyclerView.ViewHolder(binding.root) {

    init {
    }

    fun bind(item : ChatVo, type : ChatType) {
        binding.apply {
            if(type == ChatType.OTHER_SAME_CHAT || type == ChatType.OTHER_SAME_TIME_CHAT) textViewName.visibility = View.GONE
            else textViewName.visibility = View.VISIBLE

            textViewName.text = item.name
            textViewMyChat.text = item.content

            val readCount = getReadCount(item.readWho.values.toList())
            textViewReadCount.text = readCount.toString()
            if(readCount == 0) textViewReadCount.visibility = View.GONE
            else textViewReadCount.visibility = View.VISIBLE

            if(type == ChatType.OTHER_SAME_TIME_CHAT) textViewChatTime.visibility = View.GONE
            else textViewChatTime.visibility = View.VISIBLE
            textViewChatTime.text = item.time
        }
    }

    private fun getReadCount(list: List<Boolean>) : Int{
        var trueCount = 0
        list.forEach { if(it) trueCount++ }
        return (list.size - trueCount)
    }
}