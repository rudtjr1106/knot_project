package com.knot.presentation.ui.sign.login

import androidx.fragment.app.viewModels
import com.kakao.sdk.user.UserApiClient
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
                    inspectEvent(it as LoginEvent)
                }
            }
        }
    }

    private fun signInKakao() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            signInKakaoApp()
        } else {
            signInKakaoEmail()
        }
    }

    private fun signInKakaoApp() {
        UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
            token?.let { viewModel.signInKaKao(it.accessToken) }
        }
    }

    private fun signInKakaoEmail() {
        UserApiClient.instance.loginWithKakaoAccount(requireContext()) { token, error ->
            token?.let { viewModel.signInKaKao(it.accessToken) }
        }
    }

    private fun inspectEvent(event: LoginEvent){
        when(event){
            LoginEvent.KaKaoLoginEvent -> signInKakao()
            LoginEvent.GoToMainEvent -> {}
            LoginEvent.GoToSignUpEvent -> {}
        }
    }

    override fun onStart() {
        super.onStart()
    }
}