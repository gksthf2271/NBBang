package com.khs.nbbang.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentHistoryBinding
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HistoryFragment : BaseFragment(){
    lateinit var mBinding : FragmentHistoryBinding
    val mViewModel: HistoryViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
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
        mBinding.recyclerView.setLayoutManager(layoutManager)

        mBinding.viewModel!!.mHistory.observe(requireActivity(), Observer {
            Log.v(TAG, "updated mHistory : $it")
            mBinding.recyclerView.adapter = HistoryRecyclerViewAdapter(
                requireActivity().supportFragmentManager,
                lifecycle,
                it,
                {
                    Log.v(TAG,"Clicked Item : ${it.id}")
                }
            )
        })
        mBinding.viewModel!!.showHistory()
    }
}