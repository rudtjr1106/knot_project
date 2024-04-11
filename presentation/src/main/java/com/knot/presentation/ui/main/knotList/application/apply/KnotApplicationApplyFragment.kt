package com.knot.presentation.ui.main.knotList.application.apply

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotApplicationApplyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KnotApplicationApplyFragment : BaseFragment<FragmentKnotApplicationApplyBinding, KnotApplicationApplyPageState, KnotApplicationApplyViewModel>(
    FragmentKnotApplicationApplyBinding::inflate
) {

    override val viewModel: KnotApplicationApplyViewModel by viewModels()

    private val knotApplicationApplyFragmentArgs : KnotApplicationApplyFragmentArgs by navArgs()

    override fun initView() {
        binding.apply {
            vm = viewModel
            viewModel.setKnotId(knotApplicationApplyFragmentArgs.knotId)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as KnotApplicationApplyEvent)
                }
            }
        }
    }

    private fun inspectEvent(event: KnotApplicationApplyEvent){
        when(event){
            KnotApplicationApplyEvent.GoToBackEvent -> findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}