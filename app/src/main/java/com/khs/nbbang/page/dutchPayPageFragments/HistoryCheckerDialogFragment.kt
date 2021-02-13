package com.khs.nbbang.page.dutchPayPageFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.FragmentHistroySaveDialogBinding
import com.khs.nbbang.history.HistoryViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HistoryCheckerDialogFragment : BaseDialogFragment(){
    val TAG = this.javaClass.name
    lateinit var mBinding: FragmentHistroySaveDialogBinding
    val mViewModel : HistoryViewModel by sharedViewModel()

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
        return inflater.inflate(R.layout.fragment_histroy_save_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG,"onViewCreated(...)")
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mViewModel
    }

    override fun onResume() {
        CURRENT_DIALOG_TYPE = DIALOG_TYPE.TYPE_HISTORY_CHECKER
        super.onResume()
    }
}