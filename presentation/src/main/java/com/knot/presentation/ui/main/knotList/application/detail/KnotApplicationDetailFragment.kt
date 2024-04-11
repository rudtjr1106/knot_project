package com.knot.presentation.ui.main.knotList.application.detail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotApplicationDetailBinding
import com.knot.presentation.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KnotApplicationDetailFragment : BaseFragment<FragmentKnotApplicationDetailBinding, KnotApplicationDetailPageState, KnotApplicationViewModel>(
    FragmentKnotApplicationDetailBinding::inflate
) {

    override val viewModel: KnotApplicationViewModel by viewModels()

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
                    inspectEvent(it as KnotApplicationEvent)
                }
            }
        }
    }

    private fun inspectEvent(event: KnotApplicationEvent){
        when(event){
            is KnotApplicationEvent.GoToApplicationEvent -> goToApplyKnot(event.knotId)
            KnotApplicationEvent.GoToBackEvent -> findNavController().popBackStack()
        }
    }

    private fun goToApplyKnot(knotId : String){

    }

    override fun onStart() {
        super.onStart()
    }
}