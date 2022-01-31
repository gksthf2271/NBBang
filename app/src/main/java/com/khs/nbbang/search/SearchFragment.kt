package com.khs.nbbang.search

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doBeforeTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentSearchHomeBinding
import com.khs.nbbang.search.response.DocumnetModel
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
        mKakaoViewModel.showSearchHistory(requireContext())

        mBinding.apply {
            cvSearch.apply {
                editSearch.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        groupKeywordHistory.visibility = View.VISIBLE
                        imgClose.visibility = View.VISIBLE
                    } else {
                        hideKeywordHistoryView()
                    }
                }
                editSearch.setOnKeyListener { v, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        hideKeywordHistoryView()
                        val searchText = (v as EditText).text.toString()
                        if (searchText.isNullOrEmpty()) {
                            Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
                            return@setOnKeyListener false
                        }
                        mKakaoViewModel.searchKeyword(requireContext(), searchText)
                        return@setOnKeyListener true
                    } else {
                        return@setOnKeyListener false
                    }
                }
                imgClose.setOnClickListener {
                    hideKeywordHistoryView()
                }
                recyclerSearchHistory.apply {
                    setHasFixedSize(true)
                    addItemDecoration(HistoryItemDecoration(20))
                    layoutManager = LinearLayoutManager(requireContext())
                }
            }
            recyclerSearchResult.apply {
                setHasFixedSize(true)
                addItemDecoration(HistoryItemDecoration(20))
                layoutManager = LinearLayoutManager(requireContext())
                adapter = SearchResultRecyclerViewAdapter(arrayListOf()){}
            }
        }
    }

    private fun hideKeywordHistoryView() {
        mBinding.cvSearch.apply{
            groupKeywordHistory.visibility = View.GONE
            imgClose.visibility = View.GONE
        }
    }

    private fun addObserver() {
        mKakaoViewModel.apply {
            mSearchResult.observe(requireActivity(), Observer { searchResult ->
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "search result -> $searchResult")
                mBinding.recyclerSearchResult.adapter =
                    SearchResultRecyclerViewAdapter(ArrayList(searchResult.documents)) { nbbHisory ->
                        LogUtil.vLog(LOG_TAG, TAG_CLASS, "Clicked Item : ${nbbHisory.id}")
                    }
            })

            mSearchHistory.observe(requireActivity(), Observer { searchHistory ->
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "SearchHistory : ${searchHistory.list}")
                mBinding.cvSearch.recyclerSearchHistory.adapter =
                    SearchKeywordHistoryAdapter(
                        ArrayList(searchHistory.list),
                        { searchHistory ->
                            LogUtil.vLog(LOG_TAG, TAG_CLASS, "Clicked Item : ${searchHistory.kakaoSearchKeyword.id}")

                        }, { removeHistory ->

                        })
            })
        }
    }

    override fun makeCustomLoadingView(): Dialog? {
        return null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}