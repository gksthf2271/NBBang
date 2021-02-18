package com.khs.nbbang

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.databinding.ActivityMainBinding
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {
    lateinit var mBinding: ActivityMainBinding
    val mPageViewModel by viewModel<PageViewModel>()
    val mLoginViewModel by viewModel<LoginViewModel>()
    val mDBViewModel by viewModel<HistoryViewModel>()

    // tags used to attach the fragments
    private val TAG_LOGIN = "login"
    private val TAG_DUTCH_PAY = "dutchPay"
    private val TAG_HISTORY = "history"
    private val TAG_MEMBER_SETTINGS = "member_settings"
    private val TAG_MY_PAGE = "my_page"
    var CURRENT_TAG = TAG_DUTCH_PAY
    var mNavItemIndex = 0

    private lateinit var mNavHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.pageViewModel = mPageViewModel
        mBinding.loginViewModel = mLoginViewModel
        initNaviView()

        if (savedInstanceState == null) {
            loadHome()
            navigateDestination()
        }
    }

    fun initNaviView() {
        mNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        group_indicator.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        addNaviListener()
    }

    private fun addNaviListener() {
        nav_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dutch_pay -> {
                    mNavItemIndex = 0
                    CURRENT_TAG = TAG_DUTCH_PAY
                }
                R.id.nav_history -> {
                    mNavItemIndex = 1
                    CURRENT_TAG = TAG_HISTORY
                }
                R.id.nav_member_settings -> {
                    mNavItemIndex = 2
                    CURRENT_TAG = TAG_MEMBER_SETTINGS
                }
                R.id.nav_my_page -> {
                    mNavItemIndex = 3
                    CURRENT_TAG = TAG_MY_PAGE
                }
                else -> mNavItemIndex = 0
            }
            selectNavMenu()
            navigateDestination()
            true
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            loadHome()
            navigateDestination()
        }
    }

    private fun loadHome() {
        Log.v(TAG, "loadHome(...)")
        mNavItemIndex = 0
        CURRENT_TAG = TAG_DUTCH_PAY
        selectNavMenu()
    }

    private fun selectNavMenu() {
        nav_view.menu.getItem(mNavItemIndex).isChecked = true
    }

    private fun navigateDestination() {
        when (CURRENT_TAG) {
            TAG_DUTCH_PAY -> mNavHostFragment.navController.navigate(R.id.action_go_to_home_menu)
            TAG_HISTORY -> mNavHostFragment.navController.navigate(R.id.action_go_to_history)
            TAG_MEMBER_SETTINGS -> mNavHostFragment.navController.navigate(R.id.action_go_to_history)
            TAG_MY_PAGE -> mNavHostFragment.navController.navigate(R.id.action_go_to_history)
        }

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    fun currentDestination(): FragmentNavigator.Destination {
        Log.v(
            TAG,
            "currentDestination : ${(mNavHostFragment.navController.currentDestination as FragmentNavigator.Destination).className}"
        )
        return mNavHostFragment.navController.currentDestination as FragmentNavigator.Destination
    }
}
