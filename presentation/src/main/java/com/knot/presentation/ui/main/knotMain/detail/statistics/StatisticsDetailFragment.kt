package com.knot.presentation.ui.main.knotMain.detail.statistics

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.knot.presentation.R
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotStatisticsDetailBinding
import com.knot.presentation.ui.main.knotMain.detail.statistics.adapter.StatisticsDetailAdapter
import com.knot.presentation.util.KnotLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticsDetailFragment : BaseFragment<FragmentKnotStatisticsDetailBinding, StatisticsDetailPageState, StatisticsDetailViewModel>(
    FragmentKnotStatisticsDetailBinding::inflate
) {

    private val statisticsDetailFragmentArgs : StatisticsDetailFragmentArgs by navArgs()
    override val viewModel: StatisticsDetailViewModel by viewModels()

    private val statisticsDetailAdapter : StatisticsDetailAdapter by lazy { StatisticsDetailAdapter() }

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewAllStatistics.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = statisticsDetailAdapter
            }

            viewModel.getDetail(statisticsDetailFragmentArgs.knotId)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.myAllStatistics.collect{
                    updateMyStatistics(it)
                }
            }
            launch {
                viewModel.uiState.teamStatisticsList.collect{
                    statisticsDetailAdapter.submitList(it)
                    if(it.isNotEmpty()) updateBestTeamStatistics()
                }
            }
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as StatisticsDetailEvent)
                }
            }
        }
    }

    private fun inspectEvent(event: StatisticsDetailEvent){
        when(event){
            StatisticsDetailEvent.GoToBackEvent -> findNavController().popBackStack()
        }
    }

    private fun updateMyStatistics(statistics : Int){
        binding.textViewMyStatisticsPercent.text = getString(R.string.main_knot_detail_only_percent, statistics)
    }

    private fun updateBestTeamStatistics(){
        binding.apply {
            textViewBestTeamName.text = viewModel.getBestTeamName()
            textViewBestTeamStatistics.text = getString(R.string.main_knot_detail_best_team_all_statistics, viewModel.getBestTeamAllStatistics())
            textViewChatStatisticsPercent.text = getString(R.string.main_knot_detail_only_percent, viewModel.getBestTeamChatStatistics())
            textViewGatheringStatisticsPercent.text = getString(R.string.main_knot_detail_only_count, viewModel.getBestTeamGatheringStatistics())
            textViewTodoStatisticsPercent.text = getString(R.string.main_knot_detail_only_object_count, viewModel.getBestTeamTodoStatistics())
        }
    }

    override fun onStart() {
        super.onStart()
    }
}