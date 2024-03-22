package com.knot.presentation.ui.main.knotMain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.KnotVo
import com.knot.presentation.databinding.RecyclerItemMainParticipatingKnotBinding
import com.knot.presentation.ui.main.knotMain.viewHolder.MainParticipatingKnotItemViewHolder
class MainParticipatingKnotAdapter(
    private val listener : MainParticipatingKnotDelegate
) : ListAdapter<KnotVo, RecyclerView.ViewHolder>(MainParticipatingKnotDiffCallBack()) {

    interface MainParticipatingKnotDelegate {

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MainParticipatingKnotItemViewHolder -> holder.bind(currentList[position], (position + 1).toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemMainParticipatingKnotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainParticipatingKnotItemViewHolder(binding, listener)
    }
}

class MainParticipatingKnotDiffCallBack : DiffUtil.ItemCallback<KnotVo>() {
    override fun areItemsTheSame(oldItem: KnotVo, newItem: KnotVo): Boolean = oldItem.knotId == newItem.knotId
    override fun areContentsTheSame(oldItem: KnotVo, newItem: KnotVo): Boolean = oldItem == newItem
}