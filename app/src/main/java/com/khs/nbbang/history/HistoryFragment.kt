package com.khs.nbbang.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentHistoryBinding
import com.khs.nbbang.history.db.*
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.ItemObj.Place
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HistoryFragment : BaseFragment(), NBBangGatewayImpl, NBBangHistoryView {
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
        initObserver()
    }

    fun initObserver() {

    }

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    override fun renderHistorys(nbbangHistory: GetNBBangHistoryResult) {
        Log.v(TAG,"TEST, renderHistorys : $nbbangHistory")
    }

    override val mNBBangDao: NBBangDao
        get() = mViewModel.mDB.value.let { it!!.nbbangDao() }
}