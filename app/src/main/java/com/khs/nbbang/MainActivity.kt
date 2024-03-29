package com.khs.nbbang

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.databinding.ActivityMainBinding
import com.khs.nbbang.history.HistoryFragment
import com.khs.nbbang.kakaoFriends.KakaoFriendsFragment
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.mypage.MyPageFragment
import com.khs.nbbang.page.DutchPayMainFragment
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.search.SearchLocalActivity
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.FragmentUtils
import com.khs.nbbang.utils.LogUtil
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var mBinding: ActivityMainBinding
    private val mLoginViewModel: LoginViewModel by viewModel()
    private val mPageViewModel: PageViewModel by viewModel()

    private val mMemberManagementViewModel: MemberManagementViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.pageViewModel = mPageViewModel
        mBinding.loginViewModel = mLoginViewModel

        mLoginViewModel.checkKakaoLoginBySdk(this)

        initNaviView()

        if (savedInstanceState == null) {
            gotoHome()
        }
    }

    private fun initNaviView() {
        val naviHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mBinding.navBottomView.setupWithNavController(naviHostFragment.navController)
//        mBinding.navBottomView.setOnNavigationItemSelectedListener(this)
        mBinding.navBottomView.setOnItemSelectedListener(this)
        addListenerAndObserver()
    }

    private fun addListenerAndObserver() {
        mLoginViewModel.gMyData.observe(this, Observer { kakaoUser ->
            LogUtil.vLog(LOG_TAG, TAG_CLASS, "gMyData : $kakaoUser")
            if (kakaoUser != null) {
                val id = kakaoUser.id
                val name = kakaoUser.properties?.get("nickname")
                val image = kakaoUser.properties?.get("profile_image")
                val thumbnail = kakaoUser.properties?.get("thumbnail_image")
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "MyData id : $id"
                            + "\n name : $name"
                            + "\n profile_image : $image"
                            + "\n thumbnail_image : $thumbnail"
                            + "\n connectedAt : ${kakaoUser.connectedAt}"
                )
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
            }
        })
    }

    private val gFinishToast by lazy {
        Toast.makeText(this, "한번 더 뒤로가기 입력 시 종료됩니다.", Toast.LENGTH_SHORT)
    }

    private fun gotoHome() {
        mBinding.navBottomView.selectedItemId = R.id.nav_dutch_pay
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "keyCode: $keyCode , event : $event")
//        val currentFragmentIdx = supportFragmentManager.fragments.size - 1
//        val currentFragment = supportFragmentManager.fragments[currentFragmentIdx]?.let {
//            it
//        }
//        LogUtil.vLog(LOG_TAG, TAG_CLASS, "fragment: $currentFragment")
//        val fragment = currentFragment as? BaseFragment
//        if (fragment != null && fragment.onKeyDown(keyCode, event))
//            return true
//        else {
//            when (keyCode) {
//                KeyEvent.KEYCODE_BACK -> {
//                    if (currentFragment is DutchPayMainFragment) {
//                        setResult(RESULT_FINISH)
//                        finish()
//                    } else {
//                        gotoHome()
//                        supportFragmentManager.clearBackStack()
//                    }
//                    return true
//                }
//            }
//        }
//        return super.onKeyDown(keyCode, event)
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        LogUtil.dLog(LOG_TAG,TAG_CLASS,"onNavigationItemSelected > item : ${item.title}")
        when(item.itemId) {
            R.id.nav_dutch_pay -> {
                FragmentUtils.loadFragment(DutchPayMainFragment(), R.id.nav_host_fragment, supportFragmentManager)
                return true
            }
            R.id.nav_history -> {
                FragmentUtils.loadFragment(HistoryFragment(), R.id.nav_host_fragment, supportFragmentManager)
                return true
            }
            R.id.nav_search -> {
                launch<SearchLocalActivity>(startForResult)
                return true
            }
            R.id.nav_kakao_friends_settings -> {
                FragmentUtils.loadFragment(KakaoFriendsFragment(), R.id.nav_host_fragment, supportFragmentManager)
                return true
            }
            R.id.nav_my_page -> {
                FragmentUtils.loadFragment(MyPageFragment(), R.id.nav_host_fragment, supportFragmentManager)
                return true
            }
        }

        return false
    }
}
