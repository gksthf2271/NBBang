package com.khs.nbbang.history

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentHistoryBinding
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.utils.LogUtil
import com.khs.nbbang.utils.ServiceUtils
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HistoryFragment : BaseFragment(){
    lateinit var mBinding : FragmentHistoryBinding
    private val mViewModel: HistoryViewModel by sharedViewModel()
    private val mLoginViewModel : LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHistoryBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mViewModel
        initView()
        addObserver()
    }

    override fun onPause() {
        super.onPause()
        mBinding.viewModel?.setCurrentMonthHistory()
    }

    override fun makeCustomLoadingView(): Dialog? {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "makeCustomLoadingView(...)")
        return null
    }

    fun initView() {
        val layoutManager = LinearLayoutManager(context)
        mBinding.historyRecyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(HistoryItemDecoration(10))
            this.layoutManager = layoutManager
        }

        mBinding.cviewSelectMonth.imgLeftIndicator.setOnClickListener {
            mBinding.viewModel?.decreaseMonth()
        }
        mBinding.cviewSelectMonth.imgRightIndicator.setOnClickListener {
            mBinding.viewModel?.increaseMonth()
        }

        mBinding.cviewSelectMonth.cview1.txtTitle.text = "모임 횟수"
        mBinding.cviewSelectMonth.cview2.txtTitle.text = "총 지출 금액"
    }

    private fun addObserver() {
        mBinding.viewModel?.let { historyViewModel ->
            historyViewModel.mShowLoadingView.observe(requireActivity(), Observer {
                when (it) {
                    true -> showLoadingView()
                    false -> hideLoadingView()
                }
            })
            historyViewModel.mHistory.observe(requireActivity(), Observer {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "updated mHistory : $it")

                mBinding.historyRecyclerView.adapter = HistoryRecyclerViewAdapter(
                    requireActivity().supportFragmentManager,
                    lifecycle,
                    (it as GetNBBangHistoryResult).nbbangHistoryList
                ) { nbbHisory ->
                    LogUtil.vLog(LOG_TAG, TAG_CLASS, "Clicked Item : ${nbbHisory.id}")
                }

                if (it.nbbangHistoryList.isNullOrEmpty()) {
                    LogUtil.vLog(LOG_TAG, TAG_CLASS, "empty List!, show emptyView")
                    mBinding.historyRecyclerView.visibility = View.GONE
                    mBinding.emptyView.visibility = View.VISIBLE
                } else {
                    LogUtil.vLog(LOG_TAG, TAG_CLASS, "show Item View")
                    mBinding.historyRecyclerView.visibility = View.VISIBLE
                    mBinding.emptyView.visibility = View.GONE
                }
                val myData = mLoginViewModel.gMyData.value
                var name = ""
                myData?.let { kakaoUser ->
                    name = kakaoUser.name
                }
                mBinding.cviewSelectMonth.cview1.txtDescription.text = it.nbbangHistoryList.size.toString()
                mBinding.cviewSelectMonth.cview2.txtDescription.text = ServiceUtils().getTotalAmountOfPayment(name, it)
                historyViewModel.updateLoadingFlag(false)
            })

            historyViewModel.mSelectMonth.observe(requireActivity(), Observer {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "selected month : $it")
                mBinding.cviewSelectMonth.txtMonth.text = "$it 월"
                historyViewModel.showHistoryByMonth(it)
            })
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                //todo 팝업 기능 추가 시 hide는 여기서 처리
            }
        }
        return false
    }
}