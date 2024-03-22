package com.knot.presentation.ui.main.knotMain.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.TodoVo
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

    fun bind(item : TodoVo) {
        binding.apply {
            textViewTodoTitle.text = item.title
            textViewTodoContent.text = root.context.getString(R.string.main_todo_content, item.startDay, item.endDay, item.content)
            textViewKnotTitle.text = item.knotTitle
        }
    }
}