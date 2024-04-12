package com.knot.presentation.ui.main.knotMain.detail.menu.applicants.detail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotApplicantsDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ApplicantsDetailFragment : BaseFragment<FragmentKnotApplicantsDetailBinding, ApplicantsDetailPageState, ApplicantsDetailViewModel>(
    FragmentKnotApplicantsDetailBinding::inflate
) {

    override val viewModel: ApplicantsDetailViewModel by viewModels()

    private val applicantsDetailFragmentArgs : ApplicantsDetailFragmentArgs by navArgs()

    override fun initView() {
        binding.apply {
            vm = viewModel

            viewModel.getData(applicantsDetailFragmentArgs.knotId, applicantsDetailFragmentArgs.userUid)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as ApplicantsDetailEvent)
                }
            }
        }
    }

    private fun inspectEvent(event: ApplicantsDetailEvent){
        when(event){
            ApplicantsDetailEvent.GoToBackEvent -> findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}