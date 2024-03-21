package com.knot.presentation.ui.main.knotMain.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemMainTodoListBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainTodoListAdapter

class MainToDoListItemViewHolder(
    private val binding: RecyclerItemMainTodoListBinding,
    private val listener: MainTodoListAdapter.MainTodoListDelegate
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
        }
    }

    fun bind(item : String) {
        binding.apply {
            textViewTodoTitle.text = item
            textViewTodoContent.text = root.context.getString(R.string.main_todo_content, "오늘", "내일", "테스트 보내기")
            textViewKnotTitle.text = "테스트"
        }
    }
}