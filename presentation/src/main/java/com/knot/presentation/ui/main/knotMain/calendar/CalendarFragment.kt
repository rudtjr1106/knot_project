package com.knot.presentation.ui.main.knotMain.calendar

import androidx.fragment.app.viewModels
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentCalendarBinding
import com.knot.presentation.databinding.FragmentKnotListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding, PageState.Default, CalendarViewModel>(
    FragmentCalendarBinding::inflate
) {

    override val viewModel: CalendarViewModel by viewModels()

    override fun initView() {
        binding.apply {
            vm = viewModel
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
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