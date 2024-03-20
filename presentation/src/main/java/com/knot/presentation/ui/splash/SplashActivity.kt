package com.knot.presentation.ui.splash

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.knot.presentation.BuildConfig
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseActivity
import com.knot.presentation.databinding.ActivitySplashBinding
import com.knot.presentation.ui.main.MainActivity
import com.knot.presentation.ui.sign.SignActivity
import com.knot.presentation.util.KnotLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, PageState.Default, SplashViewModel>(
    ActivitySplashBinding::inflate) {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 5000
    }

    override val viewModel: SplashViewModel by viewModels()
    override fun initView() {

        binding.apply {
            vm = viewModel
            permissionCheck()
            viewModel.checkLogin(getToken())
        }
    }

    override fun initState() {
        super.initState()

        repeatOnStarted {
            launch {
                viewModel.eventFlow.collect {
                    sortEvent(it as SplashEvent)
                }
            }
        }
    }

    private fun sortEvent(event: SplashEvent){
        when(event){
            SplashEvent.GoToLoginEvent -> goToLogin()
            SplashEvent.GoToMainEvent -> goToMain()
        }
    }

    private fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToLogin(){
        val intent = Intent(this, SignActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun getToken() : String {
        val sharedPreferences = getSharedPreferences(BuildConfig.shared_preferences_name, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        KnotLog.D(token.toString())
        return token.toString()
    }
}