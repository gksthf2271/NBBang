package com.khs.nbbang.shareMessage

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.FragmentShareDialogBinding
import kotlinx.android.synthetic.main.fragment_share_dialog.view.*

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
        return inflater.inflate(R.layout.fragment_share_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
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