package com.khs.nbbang.history.itemView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentHistorySecondGroupBinding
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils
import kotlinx.android.synthetic.main.cview_pie_chart.view.*
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

    fun initView() {
        val pieEntryList = arrayListOf<PieEntry>()
        var totalPrice = 0
        for (item in mHistoryItem.place) {
            totalPrice += item.price
            pieEntryList.add(PieEntry(item.price.toFloat(), item.placeName, null, item.peopleList))
        }
        mBinding.customPieChart.setData(pieEntryList, totalPrice)

        mBinding.groupInfo1.txt_title.text = "지출"
        mBinding.groupInfo2.txt_title.text = "참석자"
        mBinding.groupInfo3.txt_title.text = "내용"

        mBinding.customPieChart.mBinding.pieChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onNothingSelected() {
                Log.v(TAG,"onNothingSelected(...)")
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                mBinding.groupInfo1.txt_description.text = NumberUtils().makeCommaNumber((e as PieEntry).value.toInt())
                mBinding.groupInfo2.txt_description.isSingleLine = false
                mBinding.groupInfo2.txt_description.text =
                    StringUtils().getPeopleList((e as PieEntry).data as ArrayList<People>)
                mBinding.groupInfo3.txt_description.text = (e as PieEntry).label
            }
        })

    }
}