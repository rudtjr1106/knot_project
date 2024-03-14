package com.knot.presentation.ui.sign

import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.knot.presentation.PageState
import com.knot.presentation.R
import com.knot.presentation.base.BaseActivity
import com.knot.presentation.databinding.ActivitySignBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignActivity : BaseActivity<ActivitySignBinding, PageState.Default, SignViewModel>(ActivitySignBinding::inflate) {

    override val viewModel: SignViewModel by viewModels()
    private lateinit var navController: NavController

    override fun initView() {

        binding.apply {
            vm = viewModel
            initNavigation()
        }
    }

    override fun initState() {
        super.initState()


    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
    }
}