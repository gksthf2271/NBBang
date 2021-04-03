package com.khs.nbbang.history.itemView

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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
import com.khs.nbbang.history.data.Place
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HistoryGroupFirstFragment(val mHistoryItem: NBBangHistory): BaseFragment() {
    lateinit var mBinding : FragmentHistoryFirstGroupBinding
    val mViewModel: HistoryViewModel by sharedViewModel()
    val PAGE_SIZE = 3

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

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }

    fun initView() {
        mBinding.viewModel ?: return

        val layoutManager = LinearLayoutManager(context)
        mBinding.recyclerView.addItemDecoration(HistoryItemDecoration(10))
        mBinding.recyclerView.layoutManager = layoutManager

        var currentPage = 1
        var pageMap = getPageMap()

        loadPage(currentPage, pageMap)

        mBinding.groupIndicatorDown.setOnClickListener {
            if (currentPage >= pageMap.keys.size) return@setOnClickListener
            currentPage = currentPage.plus(1)
            loadPage(currentPage, pageMap)
        }

        mBinding.groupIndicatorUp.setOnClickListener {
            if (currentPage <= 1) return@setOnClickListener
            currentPage = currentPage.minus(1)
            loadPage(currentPage, pageMap)
        }
    }

    private fun loadPage(page: Int, pageMap : HashMap<Int, ArrayList<Place>>) {
        Log.v(TAG,"loadPage(...) page : $page, pageSize : ${pageMap.keys.size}")
        mBinding.recyclerView.adapter = HistoryItemRecyclerViewAdapter(
            pageMap.get(page-1) ?: arrayListOf()
        ) {
            Log.v(TAG, "Clicked Item : ${it.id}")
        }
        updateIndicator(page, pageMap.size)
    }

    private fun updateIndicator (currentPage : Int, pageSize : Int) {
        if (pageSize == 1) {
            mBinding.groupIndicatorUp.visibility = View.INVISIBLE
            mBinding.groupIndicatorDown.visibility = View.INVISIBLE
        } else if(pageSize > 1 && currentPage == 1) {
            mBinding.groupIndicatorUp.visibility = View.INVISIBLE
            mBinding.groupIndicatorDown.visibility = View.VISIBLE
        } else if(pageSize > 1 && currentPage != 1 && currentPage != pageSize) {
            mBinding.groupIndicatorUp.visibility = View.VISIBLE
            mBinding.groupIndicatorDown.visibility = View.VISIBLE
        } else if(pageSize > 1 && currentPage == pageSize) {
            mBinding.groupIndicatorUp.visibility = View.VISIBLE
            mBinding.groupIndicatorDown.visibility = View.INVISIBLE
        }
    }

    private fun getPageMap() : HashMap<Int, ArrayList<Place>>{
        var pageHashMap : HashMap<Int, ArrayList<Place>> = hashMapOf()
        var pageIndex = 0
        for (place in mHistoryItem.place) {
            pageHashMap.get(pageIndex) ?: pageHashMap.put(pageIndex, arrayListOf())
            val currentArrayList = pageHashMap.get(pageIndex) as ArrayList<Place>
            if (currentArrayList.size < PAGE_SIZE) {
                currentArrayList.add(place)
                if (currentArrayList.size >= PAGE_SIZE) pageIndex++
            }
        }
        return pageHashMap
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