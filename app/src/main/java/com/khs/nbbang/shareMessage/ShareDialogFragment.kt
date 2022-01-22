package com.khs.nbbang.shareMessage

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.FragmentShareDialogBinding

class ShareDialogFragment : BaseDialogFragment(DIALOG_TYPE.TYPE_SHARE_RESULT) {
    lateinit var mBinding: FragmentShareDialogBinding

    companion object {
        @Volatile
        private var instance: ShareDialogFragment? = null

        @JvmStatic
        fun getInstance(): ShareDialogFragment =
            instance ?: synchronized(this) {
                instance
                    ?: ShareDialogFragment().also {
                        instance = it
                    }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentShareDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        mBinding.btnClose.setOnClickListener{
            dismiss()
        }
        mBinding.btnShare.setOnClickListener{
            dismiss()
        }
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