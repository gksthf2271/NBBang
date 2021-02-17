package com.khs.nbbang.history.itemView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentHistoryFirstGroupBinding
import com.khs.nbbang.history.HistoryItemRecyclerViewAdapter
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.history.data.NBBangHistory
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HistoryGroupFirstFragment(val mHistoryItem: NBBangHistory): BaseFragment() {
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
        mBinding.viewModel ?: return

        val layoutManager = LinearLayoutManager(context)
        mBinding.recyclerView.addItemDecoration(HistoryItemDecoration(10))
        mBinding.recyclerView.layoutManager = layoutManager

        mBinding.recyclerView.adapter = HistoryItemRecyclerViewAdapter(
            mHistoryItem
        ) {
            Log.v(TAG, "Clicked Item : ${it.id}")
        }
    }
}