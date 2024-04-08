package com.knot.presentation.ui.main.createOrEditKnot

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.domain.vo.CategoryVo
import com.knot.domain.vo.TeamUserVo
import com.knot.presentation.R
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentCreateOrEditKnotBinding
import com.knot.presentation.ui.main.createOrEditKnot.adapter.CategoryAdapter
import com.knot.presentation.ui.main.createOrEditKnot.adapter.InvitedMemberAdapter
import com.knot.presentation.util.KnotLog
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

    private val categoryAdapter : CategoryAdapter by lazy {
        CategoryAdapter(object : CategoryAdapter.CategoryDelegate{
            override fun onClickCategory(categoryVo: CategoryVo) {
                viewModel.onClickCategory(categoryVo)
            }
        })
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewCategory.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = categoryAdapter
            }

            recyclerViewInvitedMember.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = invitedMemberAdapter
            }
            initCategory()
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
                viewModel.uiState.categoryList.collect {
                    categoryAdapter.submitList(it)
                }
            }
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as CreateOrEditKnotEvent)
                }
            }
        }
    }

    private fun initCategory(){
        viewModel.updateEmptyCategoryList(resources.getStringArray(R.array.knot_category).toList())
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