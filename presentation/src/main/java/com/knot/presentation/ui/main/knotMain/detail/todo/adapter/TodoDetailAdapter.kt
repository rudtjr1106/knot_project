package com.knot.presentation.ui.main.knotMain.detail.todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.TodoDetailVo
import com.knot.presentation.databinding.RecyclerItemKnotDetailTodoParentBinding
import com.knot.presentation.ui.main.knotMain.detail.todo.viewHolder.TodoDetailViewHolder

class TodoDetailAdapter(
    private val listener : TodoDetailDelegate
) : ListAdapter<TodoDetailVo, RecyclerView.ViewHolder>(TodoDetailDiffCallBack()) {

    interface TodoDetailDelegate {
        fun onClickComplete(request: CheckKnotTodoRequest)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TodoDetailViewHolder -> holder.bind(currentList[position].todoList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemKnotDetailTodoParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoDetailViewHolder(binding, listener)
    }
}

class TodoDetailDiffCallBack : DiffUtil.ItemCallback<TodoDetailVo>() {
    override fun areItemsTheSame(oldItem: TodoDetailVo, newItem: TodoDetailVo): Boolean = oldItem.todoList == newItem.todoList
    override fun areContentsTheSame(oldItem: TodoDetailVo, newItem: TodoDetailVo): Boolean = oldItem == newItem
}