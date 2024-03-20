package com.knot.presentation.ui.sign.login

import android.content.Context
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.user.UserApiClient
import com.knot.presentation.BuildConfig
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
            LoginEvent.GoToSignUpEvent -> goToSignUp()
            is LoginEvent.SaveUserTokenEvent -> saveToken(event.token)
        }
    }

    private fun goToSignUp(){
        val action = LoginFragmentDirections.actionLoginToSignUp()
        findNavController().navigate(action)
    }

    private fun saveToken(token : String){
        val sharedPreferences = requireContext().getSharedPreferences(BuildConfig.shared_preferences_name, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    override fun onStart() {
        super.onStart()
    }
}