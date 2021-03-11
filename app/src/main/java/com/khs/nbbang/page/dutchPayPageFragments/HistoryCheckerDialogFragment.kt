package com.khs.nbbang.page.dutchPayPageFragments

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.FragmentHistorySaveDialogBinding
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HistoryCheckerDialogFragment : BaseDialogFragment(){
    val TAG = this.javaClass.simpleName
    lateinit var mBinding: FragmentHistorySaveDialogBinding
    val mHistoryViewModel : HistoryViewModel by sharedViewModel()
    val mPageViewModel : PageViewModel by sharedViewModel()

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
        return inflater.inflate(R.layout.fragment_history_save_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG,"onViewCreated(...)")
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mHistoryViewModel

        initView()
    }

    override fun onResume() {
        CURRENT_DIALOG_TYPE = DIALOG_TYPE.TYPE_HISTORY_CHECKER
        super.onResume()
    }

    fun initView() {
        mBinding.btnSave.setOnClickListener {
            mPageViewModel.saveHistory()
            copy()
            dismiss()
        }

        mBinding.btnClose.setOnClickListener {
            copy()
            dismiss()
        }
    }

    fun copy() {
        val clipboard =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        val clip: ClipData = ClipData.newPlainText(
            requireArguments().getCharSequence(ResultPageFragment().KEY_TITLE),
            requireArguments().getCharSequence(ResultPageFragment().KEY_DESCRIPTION)
        )
        clipboard.let {
            it!!.setPrimaryClip(clip)
            Toast.makeText(requireContext(),"클립보드에 영수증이 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}