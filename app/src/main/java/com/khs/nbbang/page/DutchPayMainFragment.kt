package com.khs.nbbang.page

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import com.khs.nbbang.animation.CustomPageTransformer
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentDutchpayHomeBinding
import com.khs.nbbang.page.dutchPayPageFragments.AddPeopleFragment
import com.khs.nbbang.page.dutchPayPageFragments.AddPlaceFragment
import com.khs.nbbang.page.dutchPayPageFragments.PeopleCountFragment
import com.khs.nbbang.page.dutchPayPageFragments.ResultPageFragment
import com.khs.nbbang.page.pager.CustomViewPagerAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.utils.KeyboardUtils
import com.khs.nbbang.utils.LogUtil
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DutchPayMainFragment : BaseFragment(){
    lateinit var mBinding : FragmentDutchpayHomeBinding
    val mViewModel: PageViewModel by sharedViewModel()

    private val mPageViewList: MutableList<BaseFragment> = mutableListOf(
        PeopleCountFragment(),
        AddPeopleFragment(),
        AddPlaceFragment(),
        ResultPageFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDutchpayHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mViewModel
        initView()
    }

    override fun makeCustomLoadingView(): Dialog? {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "makeCustomLoadingView(...)")
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        mBinding.viewPager.apply {
            adapter = CustomViewPagerAdapter(
                requireActivity().supportFragmentManager,
                lifecycle,
                mPageViewList
            )
            currentItem = 0

            val checkMemberCountToast = Toast.makeText(context, "참여 인원을 설정해주세요.", Toast.LENGTH_SHORT)

            this[0].setOnTouchListener{ _, _ ->
                KeyboardUtils.hideKeyboard(requireView(), requireContext())
                mBinding.viewModel?.let { pageViewModel ->
                    if ( pageViewModel.mNBBLiveData.value!!.mMemberCount <= 0) {
                        checkMemberCountToast.show()
                        return@setOnTouchListener true
                    }
                }
                return@setOnTouchListener false
            }

            offscreenPageLimit = mPageViewList.size - 2
            setPageTransformer(CustomPageTransformer(this, mBinding.containerPagerIndicator))
            mBinding.viewIndicator.setViewPager2(this)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        when(keyCode) {
//            KeyEvent.KEYCODE_BACK -> {
//                if(mPageViewList[mBinding.viewPager.currentItem].onKeyDown(keyCode, event)) return true
//            }
//        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.viewModel?.clearPageViewModel()
    }
}