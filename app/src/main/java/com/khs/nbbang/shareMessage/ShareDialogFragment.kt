package com.khs.nbbang.shareMessage

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.FragmentHistoryBinding

class ShareDialogFragment : BaseDialogFragment(DIALOG_TYPE.TYPE_SHARE_RESULT) {
    lateinit var mBinding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        initView()
    }

    fun initView() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (this.isAdded) {
                    dismiss()
                    return true
                }
            }
        }
        return false
    }
}