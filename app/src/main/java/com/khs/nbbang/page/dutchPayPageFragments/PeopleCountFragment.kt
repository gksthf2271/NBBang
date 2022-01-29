package com.khs.nbbang.page.dutchPayPageFragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentPeopleCountBinding
import com.khs.nbbang.page.viewModel.PageViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel


class PeopleCountFragment : BaseFragment() {
    lateinit var mBinding: FragmentPeopleCountBinding
    val mViewModel : PageViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentPeopleCountBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mViewModel
        initView()
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG_CLASS,"makeCustomLoadingView(...)")
        return null
    }

    fun initView() {
        mBinding.viewModel?.let { pageViewModel ->
            pageViewModel.mNBBLiveData.observe(requireActivity(), Observer {
                mBinding.txtCount.text = it.mMemberCount.toString()
            })
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                //todo 팝업 기능 추가 시 hide는 여기서 처리
            }
        }
        return false
    }
}