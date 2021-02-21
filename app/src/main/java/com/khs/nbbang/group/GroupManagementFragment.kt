package com.khs.nbbang.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentGroupManagementBinding
import com.khs.nbbang.history.HistoryViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class GroupManagementFragment : BaseFragment() {
    lateinit var mBinding : FragmentGroupManagementBinding
    val mViewModel: HistoryViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mViewModel
        initView()
        addObserver()
    }

    fun initView() {
    }

    private fun addObserver() {
        mBinding.viewModel ?: return
    }
}