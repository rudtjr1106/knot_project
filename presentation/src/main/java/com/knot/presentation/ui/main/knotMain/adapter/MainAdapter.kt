package com.knot.presentation.ui.main.knotMain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.enums.MainVIewType
import com.knot.domain.vo.MainLayoutVo
import com.knot.presentation.databinding.RecyclerLayoutMainParticipatingKnotBinding
import com.knot.presentation.databinding.RecyclerLayoutMainTodoListBinding
import com.knot.presentation.databinding.RecyclerLayoutMainTopBinding
import com.knot.presentation.ui.main.knotMain.viewHolder.MainParticipatingKnotViewHolder
import com.knot.presentation.ui.main.knotMain.viewHolder.MainToDoListViewHolder
import com.knot.presentation.ui.main.knotMain.viewHolder.MainTopViewHolder

class MainAdapter(
    private val listener : MainDelegate
) : ListAdapter<MainLayoutVo, RecyclerView.ViewHolder>(MainDiffCallBack()) {

    interface MainDelegate {

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MainTopViewHolder -> holder.bind(currentList[position].todoList)
            is MainParticipatingKnotViewHolder -> holder.bind(currentList[position].participatingKnotList)
            is MainToDoListViewHolder -> holder.bind(currentList[position].todoList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(MainVIewType.valueOf(viewType)){
            MainVIewType.TOP -> {
                val binding = RecyclerLayoutMainTopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MainTopViewHolder(binding, listener)
            }
            MainVIewType.PARTICIPATING_KNOT -> {
                val binding = RecyclerLayoutMainParticipatingKnotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MainParticipatingKnotViewHolder(binding, listener)
            }
            MainVIewType.TODO_LIST -> {
                val binding = RecyclerLayoutMainTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MainToDoListViewHolder(binding, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type.type
    }

}

class MainDiffCallBack : DiffUtil.ItemCallback<MainLayoutVo>() {
    override fun areItemsTheSame(oldItem: MainLayoutVo, newItem: MainLayoutVo): Boolean = oldItem.type == newItem.type
    override fun areContentsTheSame(oldItem: MainLayoutVo, newItem: MainLayoutVo): Boolean = oldItem == newItem
}