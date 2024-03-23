package com.knot.presentation.ui.main.knotMain.calendar

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentCalendarBinding
import com.knot.presentation.ui.main.knotMain.calendar.adapter.CalendarAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding, CalendarPageState, CalendarViewModel>(
    FragmentCalendarBinding::inflate
) {

    override val viewModel: CalendarViewModel by viewModels()

    private val calendarAdapter : CalendarAdapter by lazy {
        CalendarAdapter(object : CalendarAdapter.CalendarDelegate{

        })
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewCalendar.apply {
                layoutManager = LinearLayoutManager(context)
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

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }
}