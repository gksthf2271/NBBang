package com.khs.nbbang.search.searchDialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.FragmentSearchHomeBinding
import com.khs.nbbang.search.KakaoLocalViewModel
import com.khs.nbbang.search.SearchResultRecyclerViewAdapter
import com.khs.nbbang.search.map.KakaoMapDialogFragment
import com.khs.nbbang.utils.LogUtil
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchDialogFragment : BaseDialogFragment(DIALOG_TYPE.TYPE_SEARCH) {
    lateinit var mBinding : FragmentSearchHomeBinding
    private val mKakaoViewModel by activityViewModel<KakaoLocalViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSearchHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mKakaoViewModel
        initView()
        addObserver()
    }

    private fun initView() {
        mBinding.apply {
            cvSearch.initView(requireActivity(), mKakaoViewModel)
            recyclerSearchResult.apply {
                setHasFixedSize(true)
                addItemDecoration(HistoryItemDecoration(20))
                layoutManager = LinearLayoutManager(requireContext())
                adapter = SearchResultRecyclerViewAdapter(arrayListOf()) { _, _ -> }
            }
        }
    }

    private fun addObserver() {
        mKakaoViewModel.apply {
            mSearchResult.observe(requireActivity(), Observer { searchResult ->
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "search result -> $searchResult")
                if (searchResult.documents.isNullOrEmpty()) {
                    showEmptyView()
                } else {
                    hideEmptyView()
                }
                mBinding.recyclerSearchResult.adapter =
                    SearchResultRecyclerViewAdapter(ArrayList(searchResult.documents)) { showMap, nbbHisory ->
                        LogUtil.vLog(LOG_TAG, TAG_CLASS, "showMap : ${showMap}, Clicked Item : ${nbbHisory.id}")
                        if (showMap) {
                            val kakaoMap = KakaoMapDialogFragment(nbbHisory)
                            if (kakaoMap.isAdded) kakaoMap.dismiss()
                            kakaoMap.show(requireActivity().supportFragmentManager, null)
                        } else {

                        }
                    }
            })

            mSearchHistory.observe(requireActivity(), Observer { searchHistory ->
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "SearchHistory : ${searchHistory.list}")
                mBinding.cvSearch.setupSearchHistoryList(searchHistory)
            })
        }
    }

    private fun showEmptyView() {
        mBinding.apply {
            emptyView.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyView() {
        mBinding.apply {
            emptyView.visibility = View.GONE
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}