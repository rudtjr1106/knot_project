package com.knot.presentation.ui.main.createOrEditKnot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CategoryVo
import com.knot.presentation.databinding.RecyclerItemCategoryBinding
import com.knot.presentation.ui.main.createOrEditKnot.viewHolder.CategoryViewHolder

class CategoryAdapter(
    private val listener : CategoryDelegate
) : ListAdapter<CategoryVo, RecyclerView.ViewHolder>(CategoryDiffCallBack()) {

    interface CategoryDelegate {
        fun onClickCategory(categoryVo: CategoryVo)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding, listener)
    }
}

class CategoryDiffCallBack : DiffUtil.ItemCallback<CategoryVo>() {
    override fun areItemsTheSame(oldItem: CategoryVo, newItem: CategoryVo): Boolean = oldItem.category == newItem.category
    override fun areContentsTheSame(oldItem: CategoryVo, newItem: CategoryVo): Boolean = oldItem == newItem
}