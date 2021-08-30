package com.khs.nbbang.history.itemView

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentHistorySecondGroupBinding
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.history.data.DutchPayPeople
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils
import kotlinx.android.synthetic.main.cview_title_description.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HistoryGroupSecondFragment(private val mHistoryItem: NBBangHistory): BaseFragment() {
    lateinit var mBinding : FragmentHistorySecondGroupBinding
    val mViewModel: HistoryViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_second_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mViewModel
        initView()
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }

    fun initView() {
        val pieEntryList = arrayListOf<PieEntry>()
        var totalPrice = 0
        val allPlaceSet = hashSetOf<String>()

        for (item in mHistoryItem.place) {
            totalPrice += item.price
            allPlaceSet.add(item.placeName)
            pieEntryList.add(PieEntry(item.price.toFloat(), item.placeName, null, item.joinPeopleList))
        }
        mBinding.customPieChart.setData(pieEntryList, totalPrice)
        loadTotalData(totalPrice, allPlaceSet, mHistoryItem.dutchPay)

        mBinding.customPieChart.mBinding.pieChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onNothingSelected() {
                Log.v(TAG,"onNothingSelected(...)")
                loadTotalData(totalPrice, allPlaceSet, mHistoryItem.dutchPay)
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                mBinding.groupInfo1.txt_title.text = "장소"
                mBinding.groupInfo2.txt_title.text = "참석자"
                mBinding.groupInfo3.txt_title.text = "지출"

                mBinding.groupInfo1.txt_description.text = (e as PieEntry).label
                mBinding.groupInfo2.txt_description.isSingleLine = false
                mBinding.groupInfo2.txt_description.text =
                    StringUtils().getPeopleList((e as PieEntry).data as ArrayList<Member>)
                mBinding.groupInfo3.txt_description.text = NumberUtils().makeCommaNumber(true, (e as PieEntry).value.toInt())
            }
        })
    }

    fun loadTotalData(
        totalPrice: Int,
        allPlace: HashSet<String>,
        dutchPeopleList: List<DutchPayPeople>
    ) {
        mBinding.groupInfo2.txt_description.isSingleLine = false
        mBinding.groupInfo1.txt_title.isSingleLine = false
        mBinding.groupInfo2.txt_title.isSingleLine = false

        mBinding.groupInfo1.txt_title.text = "모든\n장소"
        mBinding.groupInfo2.txt_title.text = "모든\n참석자"
        mBinding.groupInfo3.txt_title.text = "총 지출"

        mBinding.groupInfo1.txt_description.text = StringUtils().listToString(allPlace.toMutableList())
        mBinding.groupInfo2.txt_description.text = StringUtils().dutchPayListToString(dutchPeopleList.toMutableList())
        mBinding.groupInfo3.txt_description.text = NumberUtils().makeCommaNumber(true, totalPrice)
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