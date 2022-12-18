package com.khs.nbbang.history.itemView

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentHistoryFirstGroupBinding
import com.khs.nbbang.history.HistoryItemRecyclerViewAdapter
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.utils.LogUtil
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HistoryGroupFirstFragment(private val mHistoryItem: NBBangHistory): BaseFragment() {
    lateinit var mBinding : FragmentHistoryFirstGroupBinding
    private val mViewModel: HistoryViewModel by activityViewModel()
    private val PAGE_SIZE = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHistoryFirstGroupBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mViewModel
        initView()
    }

    override fun makeCustomLoadingView(): Dialog? {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "makeCustomLoadingView(...)")
        return null
    }

    fun initView() {
        mBinding.viewModel ?: return

        val layoutManager = LinearLayoutManager(context)
        mBinding.recyclerView.addItemDecoration(HistoryItemDecoration(10))
        mBinding.recyclerView.layoutManager = layoutManager

        var currentPage = 1
        val pageMap = getPageMap()

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
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "loadPage(...) page : $page, pageSize : ${pageMap.keys.size}")
        mBinding.recyclerView.adapter = HistoryItemRecyclerViewAdapter(
            pageMap.get(page-1) ?: arrayListOf()
        ) { place ->
            LogUtil.vLog(LOG_TAG, TAG_CLASS, "Clicked Item : ${place.placeName}")
            val dialog = DetailBottomItemView(place)
            dialog.show(requireActivity().supportFragmentManager, null)
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
        val pageHashMap : HashMap<Int, ArrayList<Place>> = hashMapOf()
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