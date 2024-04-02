package com.knot.presentation.ui.main.knotMain.detail.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.enums.ChatType
import com.knot.domain.vo.ChatLayoutVo
import com.knot.presentation.databinding.RecyclerItemChatDivideLineBinding
import com.knot.presentation.databinding.RecyclerItemMyChatBinding
import com.knot.presentation.databinding.RecyclerItemOtherChatBinding
import com.knot.presentation.ui.main.knotMain.detail.chat.viewHolder.DivideChatViewHolder
import com.knot.presentation.ui.main.knotMain.detail.chat.viewHolder.MyChatViewHolder
import com.knot.presentation.ui.main.knotMain.detail.chat.viewHolder.OtherChatViewHolder
import com.knot.presentation.util.KnotLog

class ChatAdapter: ListAdapter<ChatLayoutVo, RecyclerView.ViewHolder>(ChatDiffCallBack()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DivideChatViewHolder -> holder.bind(currentList[position].chat.date)
            is MyChatViewHolder -> holder.bind(currentList[position].chat, currentList[position].type)
            is OtherChatViewHolder -> holder.bind(currentList[position].chat, currentList[position].type)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(ChatType.valueOf(viewType)){
            ChatType.DIVIDE -> {
                val binding = RecyclerItemChatDivideLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DivideChatViewHolder(binding)
            }
            ChatType.MY_SAME_TIME_CHAT,
            ChatType.MY_CHAT -> {
                val binding = RecyclerItemMyChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyChatViewHolder(binding)
            }
            ChatType.OTHER_SAME_TIME_CHAT,
            ChatType.OTHER_SAME_CHAT,
            ChatType.OTHER_CHAT -> {
                val binding = RecyclerItemOtherChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OtherChatViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type.type
    }

}

class ChatDiffCallBack : DiffUtil.ItemCallback<ChatLayoutVo>() {
    override fun areItemsTheSame(oldItem: ChatLayoutVo, newItem: ChatLayoutVo): Boolean{
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: ChatLayoutVo, newItem: ChatLayoutVo): Boolean = oldItem.chat == newItem.chat
}