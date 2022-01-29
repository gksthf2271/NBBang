package com.khs.nbbang.page.dutchPayPageFragments

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.FragmentHistorySaveDialogBinding
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.utils.LogUtil
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HistoryCheckerDialogFragment : BaseDialogFragment(DIALOG_TYPE.TYPE_HISTORY_CHECKER){
    lateinit var mBinding: FragmentHistorySaveDialogBinding
    private val mHistoryViewModel : HistoryViewModel by sharedViewModel()
    private val mPageViewModel : PageViewModel by sharedViewModel()

    companion object {
        @Volatile
        private var instance: HistoryCheckerDialogFragment? = null

        @JvmStatic
        fun getInstance(): HistoryCheckerDialogFragment =
            instance ?: synchronized(this) {
                instance
                    ?: HistoryCheckerDialogFragment().also {
                        instance = it
                    }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHistorySaveDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onViewCreated(...)")
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mHistoryViewModel

        initView()
    }

    fun initView() {
        mBinding.btnSave.setOnClickListener {
            if (!mPageViewModel.gIsSavedResult) {
                mPageViewModel.saveHistory()
            } else {
                Toast.makeText(requireContext(),"이미 저장된 영수증입니다.", Toast.LENGTH_SHORT).show()
            }
            copy()
            dismiss()
        }

        mBinding.btnClose.setOnClickListener {
            copy()
            dismiss()
        }
    }

    private fun copy() {
        val clipboard =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        val clip: ClipData = ClipData.newPlainText(
            requireArguments().getCharSequence(ResultPageFragment().KEY_TITLE),
            requireArguments().getCharSequence(ResultPageFragment().KEY_DESCRIPTION)
        )
        clipboard?.let {
            it.setPrimaryClip(clip)
            Toast.makeText(requireContext(),"클립보드에 영수증이 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
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