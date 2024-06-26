package com.knot.presentation.ui.main.knotMain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.TodoVo
import com.knot.presentation.databinding.RecyclerItemMainTodoListBinding
import com.knot.presentation.ui.main.knotMain.viewHolder.MainToDoListItemViewHolder

class MainTodoListAdapter(
    private val listener : MainTodoListDelegate
) : ListAdapter<TodoVo, RecyclerView.ViewHolder>(MainTodoListDiffCallBack()) {

    interface MainTodoListDelegate {
        fun onClickComplete(request: CheckKnotTodoRequest)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MainToDoListItemViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemMainTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainToDoListItemViewHolder(binding, listener)
    }
}

class MainTodoListDiffCallBack : DiffUtil.ItemCallback<TodoVo>() {
    override fun areItemsTheSame(oldItem: TodoVo, newItem: TodoVo): Boolean = oldItem.todoId == newItem.todoId
    override fun areContentsTheSame(oldItem: TodoVo, newItem: TodoVo): Boolean = oldItem == newItem
}