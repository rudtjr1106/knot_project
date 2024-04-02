package com.knot.presentation.ui.main.knotMain.viewHolder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.TodoVo
import com.knot.presentation.databinding.RecyclerLayoutMainTodoListBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainAdapter
import com.knot.presentation.ui.main.knotMain.adapter.MainTodoListAdapter

class MainToDoListViewHolder(
    private val binding: RecyclerLayoutMainTodoListBinding,
    private val listener: MainAdapter.MainDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private val mainTodoListAdapter : MainTodoListAdapter by lazy {
        MainTodoListAdapter(object : MainTodoListAdapter.MainTodoListDelegate{
            override fun onClickComplete(request: CheckKnotTodoRequest) {
                listener.onClickCheckButton(request)
            }
        })
    }

    init {
        binding.apply {
            recyclerViewParticipatingKnot.apply {
                layoutManager = LinearLayoutManager(root.context)
                adapter = mainTodoListAdapter
            }
        }
    }

    fun bind(item : List<TodoVo>) {
        mainTodoListAdapter.submitList(item)
    }
}