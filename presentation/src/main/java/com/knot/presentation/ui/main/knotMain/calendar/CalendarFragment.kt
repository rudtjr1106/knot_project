package com.knot.presentation.ui.main.knotMain.calendar

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentCalendarBinding
import com.knot.presentation.ui.main.knotMain.calendar.adapter.CalendarAdapter
import com.knot.presentation.ui.common.bottomsheet.CommonBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding, CalendarPageState, CalendarViewModel>(
    FragmentCalendarBinding::inflate
) {

    override val viewModel: CalendarViewModel by viewModels()

    private val calendarAdapter : CalendarAdapter by lazy {
        CalendarAdapter(object : CalendarAdapter.CalendarDelegate{
            override fun onClickBack() {
                findNavController().popBackStack()
            }

            override fun onClickKnotTitle() {
                viewModel.showKnotListBottomSheet()
            }
        })
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewCalendar.apply {
                layoutManager = LinearLayoutManager(context)
                itemAnimator = null
                adapter = calendarAdapter
            }

            viewModel.getData()
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.calendarLayoutList.collect {
                    calendarAdapter.submitList(it)
                }
            }
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as CalendarEvent)
                }
            }
        }
    }

    private fun inspectEvent(event : CalendarEvent){
        when(event){
            is CalendarEvent.ShowBottomSheet -> showBottomSheet(event.list)
        }
    }

    private fun showBottomSheet(list: List<String>){
        CommonBottomSheet.newInstance(list) {
            viewModel.onClickMenu(it)
        }.show(parentFragmentManager, "")
    }

    override fun onStart() {
        super.onStart()
    }
}