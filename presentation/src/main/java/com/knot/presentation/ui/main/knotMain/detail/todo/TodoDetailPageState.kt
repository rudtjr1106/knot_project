package com.knot.presentation.ui.main.knotMain.detail.todo

import com.knot.domain.vo.TodoDetailVo
import com.knot.presentation.PageState
import kotlinx.coroutines.flow.StateFlow

data class TodoDetailPageState(
    val todoList : StateFlow<List<TodoDetailVo>>
) : PageState
