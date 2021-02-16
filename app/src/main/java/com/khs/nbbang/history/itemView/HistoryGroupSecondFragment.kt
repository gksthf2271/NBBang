package com.khs.nbbang.history.itemView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentHistoryFirstGroupBinding
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.history.data.NBBangHistory
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HistoryGroupSecondFragment(val mHistoryItem: NBBangHistory): BaseFragment() {
    lateinit var mBinding : FragmentHistoryFirstGroupBinding
    val mViewModel: HistoryViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_first_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mViewModel
        initView()
    }

    fun initView() {

    }
}