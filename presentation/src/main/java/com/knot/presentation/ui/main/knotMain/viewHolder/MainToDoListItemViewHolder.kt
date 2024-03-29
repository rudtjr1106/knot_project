package com.knot.presentation.ui.main.knotMain.viewHolder

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.domain.vo.TodoVo
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemMainTodoListBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainTodoListAdapter
import com.knot.presentation.util.DateTimeFormatter
import com.knot.presentation.util.UserInfo

class MainToDoListItemViewHolder(
    private val binding: RecyclerItemMainTodoListBinding,
    private val listener: MainTodoListAdapter.MainTodoListDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var todoVo: TodoVo
    init {
        binding.apply {
            imageButtonCheck.setOnClickListener {
                if(todoVo.userId == UserInfo.info.id) { listener.onClickComplete(getRequest()) }
            }
        }
    }

    fun bind(item : TodoVo) {
        todoVo = item
        binding.apply {
            if(item.complete){
                imageButtonCheck.setBackgroundResource(R.drawable.ic_check_filled_ffcc00)
                viewCompleteTodo.visibility = View.VISIBLE
                textViewTodoTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                textViewTodoContent.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            textViewTodoTitle.text = item.title
            textViewTodoContent.text = root.context.getString(R.string.main_todo_content,
                DateTimeFormatter.getMonthAndDayKor(item.startDay), DateTimeFormatter.getMonthAndDayKor(item.endDay), item.content)
            textViewKnotTitle.text = item.knotTitle
        }
    }

    private fun getRequest(): CheckKnotTodoRequest {
        return CheckKnotTodoRequest(
            knotId = todoVo.knotId,
            todoId = todoVo.todoId,
            complete = !todoVo.complete
        )
    }
}