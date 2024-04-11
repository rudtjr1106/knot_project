package com.knot.presentation.ui.main.knotList.application.detail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotApplicationDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KnotApplicationDetailFragment : BaseFragment<FragmentKnotApplicationDetailBinding, KnotApplicationDetailPageState, KnotApplicationDetailViewModel>(
    FragmentKnotApplicationDetailBinding::inflate
) {

    override val viewModel: KnotApplicationDetailViewModel by viewModels()

    private val knotApplicationDetailFragmentArgs : KnotApplicationDetailFragmentArgs by navArgs()

    override fun initView() {
        binding.apply {
            vm = viewModel

            viewModel.getKnotDetail(knotApplicationDetailFragmentArgs.knotId)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as KnotApplicationDetailEvent)
                }
            }
        }
    }

    private fun inspectEvent(event: KnotApplicationDetailEvent){
        when(event){
            is KnotApplicationDetailEvent.GoToApplicationEvent -> goToApplyKnot(event.knotId, event.knotTitle)
            KnotApplicationDetailEvent.GoToBackEvent -> findNavController().popBackStack()
        }
    }

    private fun goToApplyKnot(knotId : String, knotTitle : String){
        val action = KnotApplicationDetailFragmentDirections.actionKnotApplicationDetailToKnotApplicationApply(knotId, knotTitle)
        findNavController().navigate(action)
    }

    override fun onStart() {
        super.onStart()
    }
}