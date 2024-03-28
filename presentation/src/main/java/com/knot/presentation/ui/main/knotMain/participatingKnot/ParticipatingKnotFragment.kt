package com.knot.presentation.ui.main.knotMain.participatingKnot

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentParticipatingKnotBinding
import com.knot.presentation.ui.main.knotMain.MainFragmentDirections
import com.knot.presentation.ui.main.knotMain.adapter.MainParticipatingKnotAdapter
import com.knot.presentation.ui.main.knotMain.participatingKnot.adapter.KnotListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ParticipatingKnotFragment : BaseFragment<FragmentParticipatingKnotBinding, ParticipatingKnotPageState, ParticipatingKnotViewModel>(
    FragmentParticipatingKnotBinding::inflate
) {

    override val viewModel: ParticipatingKnotViewModel by viewModels()

    private val knotListAdapter : KnotListAdapter by lazy {
        KnotListAdapter(object : KnotListAdapter.KnotListDelegate{
            override fun onClickKnot(id: String) {
                goToKnotDetail(id)
            }
        })
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewKnotList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = knotListAdapter
            }

            viewModel.getKnotData()
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.knotList.collect {
                    knotListAdapter.submitList(it)
                }
            }

            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as ParticipatingKnotEvent)
                }
            }
        }
    }

    private fun inspectEvent(event: ParticipatingKnotEvent){
        when(event){
            ParticipatingKnotEvent.GoToBack -> findNavController().popBackStack()
        }
    }

    private fun goToKnotDetail(knotId : String){
        val action = ParticipatingKnotFragmentDirections.actionParticipatingKnotToKnotDetail(knotId)
        findNavController().navigate(action)
    }

    override fun onStart() {
        super.onStart()
    }
}