package com.knot.presentation.ui.main.knotMain.detail.menu.applicants

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.domain.vo.ApplicantUserVo
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotApplicantsBinding
import com.knot.presentation.ui.main.knotMain.detail.menu.applicants.adapter.KnotApplicantsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KnotApplicantsFragment : BaseFragment<FragmentKnotApplicantsBinding, KnotApplicantsPageState, KnotApplicantsViewModel>(
    FragmentKnotApplicantsBinding::inflate
) {

    override val viewModel: KnotApplicantsViewModel by viewModels()

    private val knotApplicantsFragmentArgs : KnotApplicantsFragmentArgs by navArgs()

    private val knotApplicantsAdapter : KnotApplicantsAdapter by lazy {
        KnotApplicantsAdapter(object : KnotApplicantsAdapter.KnotApplicantDelegate{
            override fun onClickCard(applicantVo: ApplicantUserVo) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewApplicants.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = knotApplicantsAdapter
            }

            viewModel.getDetail(knotApplicantsFragmentArgs.knotId)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.knotDetail.collect{
                    knotApplicantsAdapter.submitList( it.applicants.values.toList())
                }
            }
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as KnotApplicantsEvent)
                }
            }
        }
    }

    private fun inspectEvent(event: KnotApplicantsEvent){
        when(event){
            KnotApplicantsEvent.GoToBackEvent -> findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}