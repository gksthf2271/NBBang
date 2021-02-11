package com.khs.nbbang

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.databinding.ActivityMainBinding
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {
    lateinit var mBinding: ActivityMainBinding
    val mPageViewModel by viewModel<PageViewModel>()
    val mLoginViewModel by viewModel<LoginViewModel>()

    // tags used to attach the fragments
    private val TAG_HOME = "home"
    private val TAG_HISTORY = "history"
    private val TAG_SETTINGS = "settings"
    var CURRENT_TAG = TAG_HOME
    var mNavItemIndex = 0

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mNavHostFragment : NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.pageViewModel = mPageViewModel
        mBinding.loginViewModel = mLoginViewModel
        initNaviView()

        if (savedInstanceState == null) {
            loadHome()
        }
    }

    fun initNaviView() {
        setSupportActionBar(toolbar)
        mNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = mNavHostFragment.navController
        mAppBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_history, R.id.nav_settings), drawer_layout)
        setupActionBarWithNavController(navController, mAppBarConfiguration)
        addNaviListener(navController)

    }

    private fun addNaviListener(navController: NavController) {
        nav_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    mNavItemIndex = 0
                    CURRENT_TAG = TAG_HOME
                    mNavHostFragment.navController.navigate(R.id.action_go_to_home_menu)
                }
                R.id.nav_history -> {
                    mNavItemIndex = 1
                    CURRENT_TAG = TAG_HISTORY
                    mNavHostFragment.navController.navigate(R.id.action_home_menu_to_history)
                }
                R.id.nav_settings -> {
                    mNavItemIndex = 2
                    CURRENT_TAG = TAG_SETTINGS
                }
                else -> mNavItemIndex = 0
            }
            menuItem.isChecked = true
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeMenuFragment) {
                toolbar.visibility = View.VISIBLE
            } else {
                toolbar.visibility = View.GONE
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
            return
        } else {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                loadHome()
            }
        }
        super.onBackPressed()
    }

    private fun loadHome(){
        Log.v(TAG,"loadHome(...)")
        mNavItemIndex = 0
        CURRENT_TAG = TAG_HOME
        selectNavMenu()
    }

    private fun selectNavMenu() {
        nav_view.menu.getItem(mNavItemIndex).isChecked = true
    }
}