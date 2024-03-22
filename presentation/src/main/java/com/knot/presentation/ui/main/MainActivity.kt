package com.knot.presentation.ui.main

import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.knot.presentation.R
import com.knot.presentation.base.BaseActivity
import com.knot.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding, MainActivityPageState, MainActivityViewModel>(
        ActivityMainBinding::inflate
    ) {

    override val viewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController

    override fun initView() {

        binding.apply {
            vm = viewModel
            initNavigation()
        }
    }

    override fun initState() {
        super.initState()
        repeatOnStarted {
            viewModel.eventFlow.collect {
                inspectEvent(it as MainEvent)
            }
        }
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            changeBottomNavigationView(destination.id)
        }
    }

    private fun inspectEvent(event: MainEvent) {
        when (event) {
            MainEvent.GoToCreateKnot -> {}
            MainEvent.GoToKnotList -> {
                navController.navigate(R.id.knotListFragment)
            }

            MainEvent.GoToMain -> {
                navController.navigate(R.id.mainFragment)
            }

            MainEvent.GoToProfile -> {
                navController.navigate(R.id.profileFragment)
            }

            MainEvent.GoToSetting -> {
                navController.navigate(R.id.settingFragment)
            }
        }
    }

    private fun changeBottomNavigationView(id: Int) {
        when (id) {
            R.id.mainFragment -> viewModel.activeMain()
            R.id.knotListFragment -> viewModel.activeKnotList()
            R.id.profileFragment -> viewModel.activeProfile()
            R.id.settingFragment -> viewModel.activieSetting()
            else -> viewModel.goneNavigation()
        }
    }
}