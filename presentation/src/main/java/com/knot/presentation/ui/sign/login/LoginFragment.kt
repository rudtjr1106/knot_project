package com.knot.presentation.ui.sign.login

import androidx.fragment.app.viewModels
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, PageState.Default, LoginViewModel>(
    FragmentLoginBinding::inflate
) {

    override val viewModel: LoginViewModel by viewModels()

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