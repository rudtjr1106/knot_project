package com.knot.presentation.ui.main.profile

import androidx.fragment.app.viewModels
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentMainBinding
import com.knot.presentation.databinding.FragmentProfileBinding
import com.knot.presentation.ui.main.knotMain.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, PageState.Default, ProfileViewModel>(
    FragmentProfileBinding::inflate
) {

    override val viewModel: ProfileViewModel by viewModels()

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