package com.knot.presentation.ui.main.knotMain.detail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.presentation.R
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotDetailBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainTodoListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KnotDetailFragment : BaseFragment<FragmentKnotDetailBinding, KnotDetailPageState, KnotDetailViewModel>(
    FragmentKnotDetailBinding::inflate
) {

    private val knotDetailFragmentArgs : KnotDetailFragmentArgs by navArgs()

    override val viewModel: KnotDetailViewModel by viewModels()

    private val todoListAdapter : MainTodoListAdapter by lazy {
        MainTodoListAdapter(object : MainTodoListAdapter.MainTodoListDelegate{
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
                adapter = todoListAdapter
            }

            viewModel.getDetail(knotDetailFragmentArgs.knotId)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.todoList.collect{
                    todoListAdapter.submitList(it)
                }
            }
            launch {
                viewModel.uiState.myAllStatistics.collect{
                    updateMyStatistics(it)
                }
            }
            launch {
                viewModel.eventFlow.collect {

                }
            }
        }
    }

    private fun updateMyStatistics(statistics : Int){
        binding.textViewMyStatisticsPercent.text = getString(R.string.main_knot_detail_only_percent, statistics)
    }

    override fun onStart() {
        super.onStart()
    }
}