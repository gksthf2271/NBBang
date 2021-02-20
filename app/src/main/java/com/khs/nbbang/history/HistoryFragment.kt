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
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.utils.ServiceUtils
import kotlinx.android.synthetic.main.cview_title_description.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HistoryFragment : BaseFragment(){
    lateinit var mBinding : FragmentHistoryBinding
    val mViewModel: HistoryViewModel by sharedViewModel()
    val mLoginViewModel : LoginViewModel by sharedViewModel()

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
        addObserver()
    }

    fun initView() {
        val layoutManager = LinearLayoutManager(context)
        mBinding.historyRecyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(HistoryItemDecoration(10))
            this.layoutManager = layoutManager
        }

        mBinding.cviewSelectMonth.imgLeftIndicator.setOnClickListener {
            mBinding.viewModel.let { it!!.decreaseMonth() }
        }
        mBinding.cviewSelectMonth.imgRightIndicator.setOnClickListener {
            mBinding.viewModel.let { it!!.increaseMonth() }
        }

        mBinding.cviewSelectMonth.cview1.txt_title.text = "모임 횟수"
        mBinding.cviewSelectMonth.cview2.txt_title.text = "총 지출 금액"
    }

    private fun addObserver() {
        mBinding.viewModel ?: return
        mBinding.viewModel!!.mHistory.observe(requireActivity(), Observer {
            Log.v(TAG, "updated mHistory : $it")
            mBinding.historyRecyclerView.adapter = HistoryRecyclerViewAdapter(
                requireActivity().supportFragmentManager,
                lifecycle,
                (it as GetNBBangHistoryResult).nbbangHistoryList
            ) {
                Log.v(TAG, "Clicked Item : ${it.id}")
            }

            mBinding.cviewSelectMonth.cview1.txt_description.text = it.nbbangHistoryList.size.toString()
            mBinding.cviewSelectMonth.cview2.txt_description.text = ServiceUtils().getTotalAmountOfPayment("김한솔", it)
        })

        mBinding.viewModel!!.mSelectMonth.observe(requireActivity(), Observer {
            mBinding.cviewSelectMonth.txtMonth.text = "$it 월"
            mBinding.viewModel!!.showHistoryByMonth(it!!)
        })
    }
}