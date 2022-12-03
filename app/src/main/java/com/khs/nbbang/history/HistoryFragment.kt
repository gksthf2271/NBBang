package com.khs.nbbang.history

import android.annotation.SuppressLint
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
import com.khs.nbbang.utils.DateUtils
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
    ): View {
        mBinding = FragmentHistoryBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mViewModel
        initView()
        addObserver()
    }

    override fun onStart() {
        super.onStart()
        mBinding.viewModel?.setSelectYearAndMonth(DateUtils.currentYear(), DateUtils.currentMonth())
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

        mBinding.cviewSelectMonth.apply {
            txtMonth.setOnClickListener {
                val selectYear = mBinding.viewModel?.mSelectDate?.value?.first
                val selectMonth = mBinding.viewModel?.mSelectDate?.value?.second
                val mDatePickerDialog = CustomMonthPicker(currentYear = selectYear, currentMonth = selectMonth, callback = { year, month ->
                        LogUtil.iLog(LOG_TAG, TAG_CLASS, "MonthPicker > year :$year, month : $month")
                        mBinding.viewModel?.setSelectYearAndMonth(year, month)
                    })

                mDatePickerDialog.show(requireActivity().supportFragmentManager, tag)
            }
            imgLeftIndicator.setOnClickListener {
                mBinding.viewModel?.decreaseMonth()
            }
            imgRightIndicator.setOnClickListener {
                mBinding.viewModel?.increaseMonth()
            }
        }

        mBinding.cviewSelectMonth.cview1.txtTitle.text = "모임 횟수"
        mBinding.cviewSelectMonth.cview2.txtTitle.text = "총 지출 금액"
    }

    @SuppressLint("SetTextI18n")
    private fun addObserver() {
        LogUtil.iLog(LOG_TAG,TAG_CLASS,"addObserver > ")
        mBinding.viewModel?.let { historyViewModel ->
            historyViewModel.mShowLoadingView.observe(requireActivity(), Observer {
                when (it) {
                    true -> showLoadingView()
                    false -> hideLoadingView()
                }
            })
            historyViewModel.mHistory.observe(requireActivity(), Observer {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "updated mHistory : $it")
                try{
                    requireActivity()
                } catch (e : IllegalStateException) {
                    historyViewModel.updateLoadingFlag(false)
                    return@Observer
                }

                mBinding.historyRecyclerView.adapter = HistoryRecyclerViewAdapter(
                    requireActivity().supportFragmentManager,
                    lifecycle,
                    (it as GetNBBangHistoryResult).nbbangHistoryList
                ) { nbbHisory ->
                    LogUtil.vLog(LOG_TAG, TAG_CLASS, "Clicked Item : ${nbbHisory.id}")
                }

                if (it.nbbangHistoryList.isEmpty()) {
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

            historyViewModel.mSelectDate.observe(requireActivity(), Observer {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "selected month : ${it.first} 년 / ${it.second} 월")
                if (it.first == DateUtils.currentYear()) {
                    mBinding.cviewSelectMonth.txtMonth.textSize = 18.0f
                    mBinding.cviewSelectMonth.txtMonth.text = "${it.second} 월"
                } else {
                    val year = it.first.toString().substring(2,4)
                    mBinding.cviewSelectMonth.txtMonth.textSize = 15.0f
                    mBinding.cviewSelectMonth.txtMonth.text = "${year}/${String.format("%02d",it.second)}"
                }
                historyViewModel.showHistoryByMonth(it.first, it.second)
            })
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                // BackKey 처리
            }
        }
        return false
    }
}