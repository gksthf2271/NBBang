package com.khs.nbbang.search

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentSearchHomeBinding
import com.khs.nbbang.utils.KeyboardUtils
import com.khs.nbbang.utils.KeyboardVisibilityUtils
import com.khs.nbbang.utils.LogUtil
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SearchFragment : BaseFragment() {
    lateinit var mBinding : FragmentSearchHomeBinding
    private val mKakaoViewModel by sharedViewModel<KakaoLocalViewModel>()

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
                adapter = SearchResultRecyclerViewAdapter(arrayListOf()){}
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
                    SearchResultRecyclerViewAdapter(ArrayList(searchResult.documents)) { nbbHisory ->
                        LogUtil.vLog(LOG_TAG, TAG_CLASS, "Clicked Item : ${nbbHisory.id}")
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

    override fun makeCustomLoadingView(): Dialog? {
        return null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}