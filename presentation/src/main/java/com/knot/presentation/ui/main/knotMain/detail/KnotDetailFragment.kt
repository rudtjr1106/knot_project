package com.knot.presentation.ui.main.knotMain.detail

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.domain.enums.CreateOrEditKnotType
import com.knot.domain.vo.CheckKnotTodoRequest
import com.knot.presentation.R
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotDetailBinding
import com.knot.presentation.ui.common.bottomsheet.CommonBottomSheet
import com.knot.presentation.ui.main.knotMain.adapter.MainTodoListAdapter
import com.knot.presentation.ui.main.knotMain.detail.adapter.TeamStatisticsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KnotDetailFragment : BaseFragment<FragmentKnotDetailBinding, KnotDetailPageState, KnotDetailViewModel>(
    FragmentKnotDetailBinding::inflate
) {

    private val knotDetailFragmentArgs : KnotDetailFragmentArgs by navArgs()

    override val viewModel: KnotDetailViewModel by viewModels()

    private val teamStatisticsAdapter : TeamStatisticsAdapter by lazy { TeamStatisticsAdapter() }

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

            recyclerViewTeamStatistics.apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = teamStatisticsAdapter
            }

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
                viewModel.uiState.otherStatisticsList.collect{
                    teamStatisticsAdapter.submitList(it)
                }
            }
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as KnotDetailEvent)
                }
            }
        }
    }

    private fun updateMyStatistics(statistics : Int){
        binding.textViewMyStatisticsPercent.text = getString(R.string.main_knot_detail_only_percent, statistics)
    }

    private fun inspectEvent(event: KnotDetailEvent){
        when(event){
            KnotDetailEvent.GoToBackEvent -> findNavController().popBackStack()
            KnotDetailEvent.GoToStatisticsEvent -> goToStatistics()
            KnotDetailEvent.GoToChatEvent -> goToChat()
            KnotDetailEvent.GoToTodoEvent -> goToTodo()
            KnotDetailEvent.ShowHostBottomSheet -> showHostBottomSheet()
            KnotDetailEvent.ShowGuestBottomSheet -> showGuestBottomSheet()
            KnotDetailEvent.GoToEditRuleRoleEvent -> goToEditRuleAndRole()
            KnotDetailEvent.GoToEditKnotEvent -> goToEditKnot()
            KnotDetailEvent.GoToKnotApplicantsEvent -> goToKnotApplicants()
        }
    }

    private fun goToStatistics(){
        val action = KnotDetailFragmentDirections.actionKnotDetailToStatisticsDetail(knotDetailFragmentArgs.knotId)
        findNavController().navigate(action)
    }

    private fun goToChat(){
        val action = KnotDetailFragmentDirections.actionKnotDetailToChatDetail(knotDetailFragmentArgs.knotId)
        findNavController().navigate(action)
    }

    private fun goToTodo(){
        val action = KnotDetailFragmentDirections.actionKnotDetailToTodoDetail(knotDetailFragmentArgs.knotId)
        findNavController().navigate(action)
    }

    private fun goToEditRuleAndRole(){
        val action = KnotDetailFragmentDirections.actionKnotDetailToEditRoleAndRule(knotDetailFragmentArgs.knotId)
        findNavController().navigate(action)
    }

    private fun goToEditKnot(){
        val action = KnotDetailFragmentDirections.actionKnotDetailToCreateOrEditKnot(knotDetailFragmentArgs.knotId, CreateOrEditKnotType.EDIT)
        findNavController().navigate(action)
    }

    private fun goToKnotApplicants(){
        val action = KnotDetailFragmentDirections.actionKnotDetailToApplicants(knotDetailFragmentArgs.knotId)
        findNavController().navigate(action)
    }

    private fun showHostBottomSheet(){
        val list = resources.getStringArray(R.array.knot_menu_host).toList()
        CommonBottomSheet.newInstance(list) {
            viewModel.onClickHostBottomSheet(it, list)
        }.show(parentFragmentManager, "")
    }

    private fun showGuestBottomSheet(){
        val list = resources.getStringArray(R.array.knot_menu_guest).toList()
        CommonBottomSheet.newInstance(list) {
            viewModel.onClickGuestBottomSheet(it, list)
        }.show(parentFragmentManager, "")
    }

    override fun onStart() {
        super.onStart()
    }
}