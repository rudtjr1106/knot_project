package com.knot.presentation.ui.main.knotMain.detail.todo

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotListBinding
import com.knot.presentation.databinding.FragmentKnotTodoDetailBinding
import com.knot.presentation.ui.main.knotMain.detail.todo.adapter.TodoDetailAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoDetailFragment : BaseFragment<FragmentKnotTodoDetailBinding, TodoDetailPageState, TodoDetailViewModel>(
    FragmentKnotTodoDetailBinding::inflate
) {

    private val todoDetailFragmentArgs : TodoDetailFragmentArgs by navArgs()

    override val viewModel: TodoDetailViewModel by viewModels()

    private val todoDetailAdapter : TodoDetailAdapter by lazy {
        TodoDetailAdapter(object : TodoDetailAdapter.TodoDetailDelegate{
            override fun onClickComplete(request: CheckKnotTodoRequest) {
                viewModel.onClickComplete(request)
            }
        })
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewTodo.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = todoDetailAdapter
                itemAnimator = null
            }

            viewModel.getKnotDetail(todoDetailFragmentArgs.knotId)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.todoList.collect{
                    todoDetailAdapter.submitList(it)
                }
            }
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as TodoDetailEvent)
                }
            }
        }
    }

    private fun inspectEvent(event: TodoDetailEvent){
        when(event){
            TodoDetailEvent.GoToBackEvent -> findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}