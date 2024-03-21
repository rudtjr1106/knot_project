package com.knot.presentation.ui.main.knotMain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.presentation.databinding.RecyclerItemMainParticipatingKnotBinding
import com.knot.presentation.ui.main.knotMain.viewHolder.MainParticipatingKnotItemViewHolder
class MainParticipatingKnotAdapter(
    private val listener : MainParticipatingKnotDelegate
) : ListAdapter<String, RecyclerView.ViewHolder>(MainParticipatingKnotDiffCallBack()) {

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

class MainParticipatingKnotDiffCallBack : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}