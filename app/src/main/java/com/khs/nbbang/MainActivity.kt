package com.khs.nbbang

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.khs.nbbang.animation.NavigationDrawerEvent
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.databinding.ActivityMainBinding
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.cview_title_description.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
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
        updateProfileInfo(null, null, null)

        mBinding.drawerLayout.setScrimColor(Color.TRANSPARENT);
        mBinding.drawerLayout.addDrawerListener(
            NavigationDrawerEvent(
                mBinding.layoutContent,
                mBinding.navView
            )
        )
        addNaviListener()
    }

    private fun addNaviListener() {
        group_indicator.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        mBinding.navView.setNavigationItemSelectedListener { menuItem ->
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

        mLoginViewModel ?: return
        mLoginViewModel!!.mMyData.observe(this, Observer {
            if (it != null) {
                Log.v(TAG, "mMyDataFrom : ${it!!}")
                if (it!! != null) {
                    var id = it!!.id
                    var name = it!!.properties?.get("nickname")
                    var image = it!!.properties?.get("profile_image")
                    var thumbnail = it!!.properties?.get("thumbnail_image")
                    Log.v(
                        TAG, "MyData id : ${id}"
                                + "\n name : ${name}"
                                + "\n profile_image : ${image}"
                                + "\n thumbnail_image : ${thumbnail}"
                    )
                    updateProfileInfo(thumbnail, name, id.toString())
                } else {
                    updateProfileInfo(null, null, null)
                }

            } else {
                Log.v(TAG, "isLogin : $it")
                updateProfileInfo(null, null, null)
            }
        })
    }

    private fun updateProfileInfo(thumbnail: String?, name: String?, id: String?) {
        var naviHeaderView = mBinding.navView.getHeaderView(0)
        GlideUtils().drawImage(naviHeaderView.img_profile, thumbnail, null)
        naviHeaderView.group_name.txt_title.text = "이름"
        naviHeaderView.group_id.txt_title.text = "계정"
        naviHeaderView.group_name.txt_description.text = name ?: "FREE USER"
        naviHeaderView.group_id.txt_description.text = id ?: "-"
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
        mBinding.navView.menu.getItem(mNavItemIndex).isChecked = true
    }

    private fun navigateDestination() {
        when (CURRENT_TAG) {
            TAG_DUTCH_PAY -> mNavHostFragment.navController.navigate(R.id.action_go_to_home_menu)
            TAG_HISTORY -> mNavHostFragment.navController.navigate(R.id.action_go_to_history)
            TAG_MEMBER_SETTINGS -> mNavHostFragment.navController.navigate(R.id.action_go_to_group_management)
            TAG_MY_PAGE -> mNavHostFragment.navController.navigate(R.id.action_go_to_my_page)
        }

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }
}
