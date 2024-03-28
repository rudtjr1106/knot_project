package com.knot.presentation.ui.common.bottomsheet

import androidx.recyclerview.widget.RecyclerView
import com.knot.presentation.databinding.ItemKnotBottomSheetBinding

class CommonBottomSheetViewHolder(
    private val binding: ItemKnotBottomSheetBinding,
    private val listener: KnotBottomSheetAdapter.Delegate
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            menu?.let {
                listener.onClickMenu(it)
            }
        }
    }

    private var menu:String? = null

    fun bind(item: String) {
        menu = item
        binding.apply {
            textViewContent.text = item
        }
    }
}