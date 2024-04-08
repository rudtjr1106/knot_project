package com.knot.presentation.ui.main.createOrEditKnot.viewHolder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CategoryVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemCategoryBinding
import com.knot.presentation.ui.main.createOrEditKnot.adapter.CategoryAdapter

class CategoryViewHolder(
    private val binding: RecyclerItemCategoryBinding,
    private val listener: CategoryAdapter.CategoryDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var categoryVo: CategoryVo
    init {
        binding.apply {
            constraintLayoutCategory.setOnClickListener {
                listener.onClickCategory(categoryVo)
            }
        }
    }

    fun bind(item : CategoryVo) {
        categoryVo = item
        binding.apply {
            textViewCategory.text = item.category
            if(categoryVo.isSelected) {
                textViewCategory.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                constraintLayoutCategory.setBackgroundResource(R.drawable.bg_rectangle_filled_ffcc00_radius_5)
            }
            else {
                textViewCategory.setTextColor(ContextCompat.getColor(root.context, R.color.black))
                constraintLayoutCategory.setBackgroundResource(R.drawable.bg_rectangle_filled_f2f2f2_radius_5)
            }
        }
    }
}