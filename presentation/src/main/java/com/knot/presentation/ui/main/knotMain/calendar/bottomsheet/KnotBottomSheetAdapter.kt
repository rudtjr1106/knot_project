package com.knot.presentation.ui.main.knotMain.calendar.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.presentation.databinding.ItemKnotBottomSheetBinding

class KnotBottomSheetAdapter(
    private val listener: Delegate,
) : ListAdapter<String, RecyclerView.ViewHolder>(KnotBottomSheetMenuDiffCallback()) {

    interface Delegate {
        fun onClickMenu(title : String)
        val selectedDialogMenuItem : String?
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is KnotBottomSheetViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemKnotBottomSheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KnotBottomSheetViewHolder(binding,listener)
    }
}

class KnotBottomSheetMenuDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}