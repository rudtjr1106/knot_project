package com.knot.presentation.ui.main.knotMain.detail.todo.viewHolder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.TodoVo
import com.knot.presentation.databinding.RecyclerItemKnotDetailTodoParentBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainTodoListAdapter
import com.knot.presentation.ui.main.knotMain.detail.todo.adapter.TodoDetailAdapter

class TodoDetailViewHolder(
    private val binding: RecyclerItemKnotDetailTodoParentBinding,
    private val listener : TodoDetailAdapter.TodoDetailDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private val mainTodoListAdapter : MainTodoListAdapter by lazy {
        MainTodoListAdapter(object : MainTodoListAdapter.MainTodoListDelegate{
            override fun onClickComplete(request: CheckKnotTodoRequest) {
                listener.onClickComplete(request)
            }
        })
    }

    init {
        binding.apply {
            recyclerViewTodoItem.apply {
                layoutManager = LinearLayoutManager(root.context)
                adapter = mainTodoListAdapter
            }
        }
    }
    fun bind(item : List<TodoVo>) {
        binding.apply {
            textViewName.text = item.first().userName
            mainTodoListAdapter.submitList(item)
        }
    }
}