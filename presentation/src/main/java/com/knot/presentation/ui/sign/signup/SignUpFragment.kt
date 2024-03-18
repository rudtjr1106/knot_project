package com.knot.presentation.ui.sign.signup

import androidx.fragment.app.viewModels
import com.kakao.sdk.user.UserApiClient
import com.knot.presentation.PageState
import com.knot.presentation.R
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentLoginBinding
import com.knot.presentation.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpPageState, SignUpViewModel>(
    FragmentSignUpBinding::inflate
) {

    override val viewModel: SignUpViewModel by viewModels()

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
                    inspectEvent(it as SignUpEvent)
                }
            }
        }
    }
    private fun inspectEvent(event: SignUpEvent){
        when(event){
            SignUpEvent.GoToMainEvent -> {}
            is SignUpEvent.UpdateIntroTextLengthEvent -> binding.textViewMaxIntroduceText.text = getString(R.string.sign_max_introduce_text, event.length)
        }
    }

    override fun onStart() {
        super.onStart()
    }
}