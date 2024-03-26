package com.knot.presentation.ui.main.knotMain.participatingKnot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.KnotVo
import com.knot.presentation.databinding.RecyclerItemParticiaptingKnotListBinding
import com.knot.presentation.ui.main.knotMain.participatingKnot.viewHolder.KnotListViewHolder

class KnotListAdapter(
    private val listener : KnotListDelegate
) : ListAdapter<KnotVo, RecyclerView.ViewHolder>(KnotListDiffCallBack()) {

    interface KnotListDelegate {

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is KnotListViewHolder -> holder.bind(currentList[position], (position + 1).toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemParticiaptingKnotListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KnotListViewHolder(binding, listener)
    }
}

class KnotListDiffCallBack : DiffUtil.ItemCallback<KnotVo>() {
    override fun areItemsTheSame(oldItem: KnotVo, newItem: KnotVo): Boolean = oldItem.knotId == newItem.knotId
    override fun areContentsTheSame(oldItem: KnotVo, newItem: KnotVo): Boolean = oldItem == newItem
}