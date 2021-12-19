package com.khs.nbbang

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.khs.nbbang.animation.NavigationDrawerEvent
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.ActivityMainBinding
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.page.viewModel.SelectMemberViewModel
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.cview_title_description.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {
    lateinit var mBinding: ActivityMainBinding
    private val mPageViewModel by viewModel<PageViewModel>()
    private val mLoginViewModel by viewModel<LoginViewModel>()
    private val mMemberManagementViewModel by viewModel<MemberManagementViewModel>()

    private val TAG_LOGIN = "login"
    private val TAG_DUTCH_PAY = "dutchPay"
    private val TAG_HISTORY = "history"
    private val TAG_MEMBER_SETTINGS = "member_settings"
    private val TAG_KAKAO_FRIENDS_SETTINGS = "kakao_friends_settings"
    private val TAG_MY_PAGE = "my_page"
    private val TAG_NONE = "none"
    private var CURRENT_TAG = TAG_DUTCH_PAY
    private var OLD_TAG = TAG_NONE
    var mNavItemIndex = 0

    private lateinit var mNavHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.pageViewModel = mPageViewModel
        mBinding.loginViewModel = mLoginViewModel
        initNaviView()

        if (savedInstanceState == null) {
            gotoHome()
        }

        mLoginViewModel.let { loginViewModel ->
            loginViewModel.checkKakaoLoginBySdk(this)
        }
    }

    fun initNaviView() {
        mNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        updateProfileInfo(null, null, null)

        mBinding.drawerLayout.setScrimColor(Color.TRANSPARENT)
        mBinding.drawerLayout.addDrawerListener(
            NavigationDrawerEvent(
                mBinding.layoutContent,
                mBinding.navView
            )
        )
        addListenerAndObserver()
    }

    private fun addListenerAndObserver() {
        group_indicator.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        mBinding.navView.setNavigationItemSelectedListener { menuItem ->
            navigateMenu(menuItem.itemId)
            true
        }

        mLoginViewModel
        mLoginViewModel.gMyData.observe(this, Observer { kakaoUser ->
            if (kakaoUser != null) {
                Log.v(TAG, "mMyDataFrom : ${kakaoUser}")
                if (kakaoUser != null) {
                    val id = kakaoUser.id
                    val name = kakaoUser.properties?.get("nickname")
                    val image = kakaoUser.properties?.get("profile_image")
                    val thumbnail = kakaoUser.properties?.get("thumbnail_image")
                    Log.v(
                        TAG, "MyData id : ${id}"
                                + "\n name : ${name}"
                                + "\n profile_image : ${image}"
                                + "\n thumbnail_image : ${thumbnail}"
                                + "\n connectedAt : ${kakaoUser.connectedAt}"
                    )
                    updateProfileInfo(thumbnail, name, kakaoUser.kakaoAccount!!.email)
                    mMemberManagementViewModel.let { memberManagementViewModel ->
                        val myData = Member(
                            id = id,
                            kakaoId = id.toString(),
                            profileImage = image,
                            thumbnailImage = thumbnail,
                            name = name ?: ""
                        )
                        memberManagementViewModel.saveKakaoMember(listOf(myData))
                    }
                } else {
                    updateProfileInfo(null, null, null)
                }

            } else {
                Log.v(TAG, "isLogin : $kakaoUser")
                updateProfileInfo(null, null, null)
            }
        })

        mBinding.navView.getHeaderView(0).setOnClickListener {
            gotoMyPage()
        }
    }

    private fun navigateMenu(menuId: Int) {
        OLD_TAG = CURRENT_TAG
        when (menuId) {
            R.id.nav_dutch_pay -> {
                mNavItemIndex = 0
                CURRENT_TAG = TAG_DUTCH_PAY
            }
            R.id.nav_history -> {
                mNavItemIndex = 1
                CURRENT_TAG = TAG_HISTORY
            }
            R.id.nav_kakao_friends_settings -> {
                mNavItemIndex = 2
                CURRENT_TAG = TAG_KAKAO_FRIENDS_SETTINGS
            }
            R.id.nav_my_page -> {
                mNavItemIndex = 3
                CURRENT_TAG = TAG_MY_PAGE
            }
            else -> mNavItemIndex = 0
        }
        navigateDestination()
    }

    private fun updateProfileInfo(thumbnail: String?, name: String?, email: String?) {
        val naviHeaderView = mBinding.navView.getHeaderView(0)
        GlideUtils().drawImageWithString(naviHeaderView.img_profile, thumbnail, null)
        naviHeaderView.group_name.txt_title.text = "이름"
        naviHeaderView.group_id.txt_title.text = "계정"
        naviHeaderView.group_name.txt_description.text = name ?: "FREE USER"
        naviHeaderView.group_id.txt_description.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.0f)
        naviHeaderView.group_id.txt_description.text = email ?: "-"
    }

    private val gFinishToast by lazy {
        Toast.makeText(this, "한번 더 뒤로가기 입력 시 종료됩니다.", Toast.LENGTH_SHORT)
    }

    override fun onBackPressed() {
    }

    private fun gotoHome() {
        Log.v(TAG, "loadHome(...)")
        mNavItemIndex = 0
        CURRENT_TAG = TAG_DUTCH_PAY
        navigateDestination()
    }

    private fun gotoMyPage() {
        mNavItemIndex = 3
        CURRENT_TAG = TAG_MY_PAGE
        navigateDestination()
    }

    private fun navigateDestination() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        navigation(CURRENT_TAG)
    }

    fun navigation(tag: String) {
        when (tag) {
            TAG_DUTCH_PAY -> mNavHostFragment.navController.navigate(R.id.action_go_to_dutch_pay)
            TAG_HISTORY -> mNavHostFragment.navController.navigate(R.id.action_go_to_history)
            TAG_KAKAO_FRIENDS_SETTINGS -> mNavHostFragment.navController.navigate(R.id.action_go_to_kakao_friends_settings)
            TAG_MY_PAGE -> mNavHostFragment.navController.navigate(R.id.action_go_to_my_page)
        }
        mBinding.navView.menu.getItem(mNavItemIndex).isChecked = true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.v(TAG, "keyCode: $keyCode , event : $event")
        val currentFragment = supportFragmentManager.fragments[0]?.let {
            it.childFragmentManager.fragments[0]
        }
        val fragment = currentFragment as? BaseFragment
        if (fragment != null && fragment.onKeyDown(keyCode, event))
            return true
        else {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        drawer_layout.closeDrawer(GravityCompat.START)
                    } else if (CURRENT_TAG == TAG_DUTCH_PAY) {
                        if (gFinishToast != null) {
                            setResult(RESULT_FINISH)
                            finish()
                        } else {
                            gFinishToast.show()
                        }
                    } else {
                        gotoHome()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
