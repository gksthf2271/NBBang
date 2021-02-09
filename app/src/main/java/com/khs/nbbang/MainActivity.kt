package com.khs.nbbang

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.databinding.ActivityMainBinding
import com.khs.nbbang.freeUser.FreeUserActivity
import com.khs.nbbang.freeUser.viewModel.PageViewModel
import com.khs.nbbang.kakaoUser.KakaoUserActivity
import com.khs.nbbang.login.LoginActivity
import com.khs.nbbang.login.LoginType
import kotlinx.android.synthetic.main.app_bar_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {
    val REQUEST_FREE_USER = 1
    lateinit var mBinding: ActivityMainBinding
    val mViewModel by viewModel<PageViewModel>()

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewModel = mViewModel
        initNaviView()
        initView()
    }

    fun initNaviView() {
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun initView() {
        val loginType = intent.getSerializableExtra(LoginActivity().KEY_LOGIN_TYPE)

        when(loginType) {
            LoginType.TYPE_FREE -> {
                launch<FreeUserActivity>(startForResult, null)
            }
            LoginType.TYPE_KAKAO -> {
                launch<KakaoUserActivity>(startForResult, null)
            }
        }
    }
}