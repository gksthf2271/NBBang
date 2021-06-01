package com.khs.nbbang.page

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.animation.ZoomOutPageTransformer
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentDutchpayHomeBinding
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.page.dutchPayPageFragments.AddPeopleFragment
import com.khs.nbbang.page.dutchPayPageFragments.AddPlaceFragment
import com.khs.nbbang.page.dutchPayPageFragments.PeopleCountFragment
import com.khs.nbbang.page.dutchPayPageFragments.ResultPageFragment
import com.khs.nbbang.page.pager.CustomViewPagerAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.utils.KeyboardUtils
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DutchPayMainFragment : BaseFragment(){
    lateinit var mBinding : FragmentDutchpayHomeBinding
    val mViewModel: PageViewModel by sharedViewModel()
    val mMemberManagementViewModel : MemberManagementViewModel by sharedViewModel()

    val mPageViewList: MutableList<BaseFragment> = mutableListOf(
        PeopleCountFragment(),
        AddPeopleFragment(),
        AddPlaceFragment(),
        ResultPageFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dutchpay_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mViewModel
        initView()
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }

    private fun initView() {
        mBinding.viewPager.adapter = CustomViewPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle,
            mPageViewList
        )
        mBinding.viewPager.currentItem = 0

        var checkMemberCountToast = Toast.makeText(context, "참여 인원을 설정해주세요.", Toast.LENGTH_SHORT)

        mBinding.viewPager.get(0).setOnTouchListener{ _, _ ->
            KeyboardUtils.hideKeyboard(requireView(), requireContext())
            mBinding.viewModel.let {
                if ( it!!.mNBBLiveData.value!!.mMemberCount <= 0) {
                    checkMemberCountToast.show()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        mBinding.viewPager.offscreenPageLimit = mPageViewList.size - 2
        mBinding.viewPager.setPageTransformer(ZoomOutPageTransformer())
        mBinding.viewIndicator.setViewPager2(mBinding.viewPager)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if(mPageViewList.get(mBinding.viewPager.currentItem).onKeyDown(keyCode, event)) return true
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.viewModel.let {
            it!!.clearPageViewModel()
        }
    }
}