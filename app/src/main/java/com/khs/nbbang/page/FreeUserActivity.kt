package com.khs.nbbang.page

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.PageActivity
import com.khs.nbbang.databinding.ActivityFreeUserBinding
import com.khs.nbbang.page.viewModel.PageViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 *  inject() 의존성 주입 - Lazy 방식
    val bb_inject1 : BB by inject()	// inject Type 유형 1 - Type by inject()
    val bb_inject2 by inject<BB>()	// inject Type 유형 2 - by inject<Type>()

 * get() 의존성 주입 - 바로 주입 방식
    var bb_get1 : BB = get()		// get Tpye 유형 1 - Type = get()
    var bb_get2 = get<BB>()		// get Type 유형 2 - get<Type>()

 * Inject와 get 방식의 차이
    inject - Lazy 방식의 주입, 해당 객체가 사용되는 시점에 의존성 주입
    get - 바로 주입, 해당 코드 실행시간에 바로 객체를 주입
 */

class FreeUserActivity : PageActivity(){
    lateinit var mBinding: ActivityFreeUserBinding
    val mViewModel by viewModel<PageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_free_user)
        mBinding.viewModel = mViewModel
        initView()
    }

    fun initView() {
        val text = when (intent.getIntExtra("input", 0)) {  // 받아온 데이터로 처리
            100 -> "성공"
            else -> "없음"
        }
        setResult(Activity.RESULT_OK, Intent().apply { putExtra("result", text) })

        mBinding.viewPager.adapter = getPageAdapter()
        mBinding.viewPager.currentItem = 0
        mBinding.viewPager.setPagingEnable(false)
        mBinding.viewPager.offscreenPageLimit = mPageViewList.size - 2
        mBinding.viewIndicator.setViewPager(mBinding.viewPager)
    }
}