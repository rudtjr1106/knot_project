package com.knot.presentation.ui.sign.login

import android.util.Log
import androidx.fragment.app.viewModels
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
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

    private lateinit var functions: FirebaseFunctions

    override val viewModel: LoginViewModel by viewModels()

    override fun initView() {
        binding.apply {
            vm = viewModel
            functions = Firebase.functions("asia-northeast3")
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
            viewModel.signInKaKao()
            Log.d("여기 프레젠", token?.accessToken.toString())
            Log.d("여기 프레젠", error.toString())

            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.d("TAG", "사용자 정보 요청 실패", error)
                }
                else if (user != null) {
                    Log.d("TAG", "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}")
                }
            }

            val functionName = "kakaoLogin"

            // Firebase Functions에 전달할 데이터 맵
            val data = hashMapOf(
                "accessToken" to token?.accessToken
            )

            // Firebase Functions 호출
            functions
                .getHttpsCallable(functionName)
                .call(data)
                .addOnSuccessListener { result ->
                    // Firebase Functions 호출 성공 시 처리
                    val uid = result.data.toString()
                    Log.d("TAG", "Firebase Functions 호출 성공: $uid")
                }
                .addOnFailureListener { e ->
                    // Firebase Functions 호출 실패 시 처리
                    Log.e("TAG", "Firebase Functions 호출 실패", e)
                }
        }
    }

    private fun signInKakaoEmail() {
        UserApiClient.instance.loginWithKakaoAccount(requireContext()) { token, error ->
            viewModel.signInKaKao()
            Log.d("여기 프레젠", token.toString())
            Log.d("여기 프레젠", error.toString())
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.d("TAG", "사용자 정보 요청 실패", error)
                }
                else if (user != null) {
                    Log.d("TAG", "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}")
                }
            }

            val functionName = "kakaoLogin"

            // Firebase Functions에 전달할 데이터 맵
            val data = hashMapOf(
                "accessToken" to token?.accessToken
            )

            // Firebase Functions 호출
            functions
                .getHttpsCallable(functionName)
                .call(data)
                .addOnSuccessListener { result ->
                    // Firebase Functions 호출 성공 시 처리
                    val uid = result.data as Map<String, Any>
                    Log.d("TAG", "Firebase Functions 호출 성공: ${uid["isNewUser"]}")
                }
                .addOnFailureListener { e ->
                    // Firebase Functions 호출 실패 시 처리
                    Log.e("TAG", "Firebase Functions 호출 실패", e)
                }
        }
    }

    private fun inspectEvent(event: LoginEvent){
        when(event){
            LoginEvent.KaKaoLoginEvent -> signInKakao()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}