package com.khs.nbbang.page.dutchPayPageFragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentResultPageBinding
import com.khs.nbbang.page.viewModel.PageViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ResultPageFragment : BaseFragment() {
    lateinit var mBinding : FragmentResultPageBinding
    val mViewModel : PageViewModel by sharedViewModel()
    val KEY_TITLE = "KEY_TITLE"
    val KEY_DESCRIPTION = "KEY_DESCRIPTION"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mViewModel
    }

    override fun onStart() {
        super.onStart()
        mBinding.txtNotifyCopy.setOnClickListener {
            showHistoryCheckerDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.viewModel.let {
            it!!.clearDutchPayMap()
            mBinding.txtResult.text = it!!.resultNBB()
        }
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }

    private fun showHistoryCheckerDialog() {
        Log.v(TAG, "showSelectPeopleDialog(...)")
        HistoryCheckerDialogFragment.getInstance().apply {
            this.arguments = Bundle().apply {
                this.putCharSequence(KEY_TITLE, this@ResultPageFragment.mBinding.txtTitle.text)
                this.putCharSequence(KEY_DESCRIPTION, this@ResultPageFragment.mBinding.txtResult.text)
            }
        }.show(requireActivity().supportFragmentManager, tag)
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