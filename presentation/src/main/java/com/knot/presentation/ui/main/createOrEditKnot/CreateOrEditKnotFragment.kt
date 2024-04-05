package com.knot.presentation.ui.main.createOrEditKnot

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.domain.vo.TeamUserVo
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentCreateOrEditKnotBinding
import com.knot.presentation.ui.main.createOrEditKnot.adapter.InvitedMemberAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateOrEditKnotFragment : BaseFragment<FragmentCreateOrEditKnotBinding, CreateOrEditKnotPageState, CreateOrEditKnotViewModel>(
    FragmentCreateOrEditKnotBinding::inflate
) {

    private val createOrEditKnotFragmentArgs : CreateOrEditKnotFragmentArgs by navArgs()

    override val viewModel: CreateOrEditKnotViewModel by viewModels()

    private val invitedMemberAdapter : InvitedMemberAdapter by lazy {
        InvitedMemberAdapter(object : InvitedMemberAdapter.InvitedMemberDelegate{
            override fun onClickCancel(member: TeamUserVo) {
                viewModel.onClickCancelMember(member)
            }
        })
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewInvitedMember.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = invitedMemberAdapter
            }
            viewModel.getData(createOrEditKnotFragmentArgs.type, createOrEditKnotFragmentArgs.knotId)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.teamList.collect {
                    invitedMemberAdapter.submitList(it.values.toList())
                }
            }
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as CreateOrEditKnotEvent)
                }
            }
        }
    }

    private fun inspectEvent(event : CreateOrEditKnotEvent){
        when(event){
            CreateOrEditKnotEvent.GoToBackEvent -> findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}