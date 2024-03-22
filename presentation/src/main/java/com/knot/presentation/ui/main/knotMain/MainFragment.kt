package com.knot.presentation.ui.main.knotMain

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentMainBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainAdapter
import com.knot.presentation.util.KnotLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainPageState, MainViewModel>(
    FragmentMainBinding::inflate
) {

    override val viewModel: MainViewModel by viewModels()

    private val mainAdapter : MainAdapter by lazy {
        MainAdapter(object : MainAdapter.MainDelegate{

        })
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewMain.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mainAdapter
            }

            viewModel.getMyInfo()
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.mainLayoutList.collect {
                    mainAdapter.submitList(it)
                }
            }

            launch {
                viewModel.eventFlow.collect {

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }
}