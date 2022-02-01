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
            cvSearch.apply {
                groupKeywordHistory.setBackgroundColor(requireContext().getColor(R.color.search_history_backgrond))
                editSearch.apply {
                    setOnEditorActionListener { v, actionId, event ->
                        return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            if (v.hasFocus()) {
                                v.clearFocus()
                            }
                            true
                        } else {
                            false
                        }
                    }
                    setOnKeyListener { v, keyCode, event ->
                        if (event.action == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)) {
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
                    doOnTextChanged { _, start, _, after ->
                        if (start != after && mKakaoViewModel.checkKeywordHistoryNotEmpty()) {
                            showKeywordHistoryView()
                        }
                    }
                    txtRemoveAll.setOnClickListener {
                        mKakaoViewModel.removeAllKeywordHistory(requireContext())
                    }
                }
                imgClose.setOnClickListener {
                    hideKeywordHistoryView()
                }
                recyclerSearchHistory.apply {
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

    private fun showKeywordHistoryView() {
        mKakaoViewModel.showSearchHistory(requireContext())
        mBinding.cvSearch.apply{
            if(!groupKeywordHistory.isVisible) {
                LogUtil.dLog(LOG_TAG, TAG_CLASS, "showKeywordHistoryView(...)")
                groupSearch.setBackgroundColor(requireContext().getColor(R.color.logo_background_color))
                groupKeywordHistory.visibility = View.VISIBLE
                imgClose.visibility = View.VISIBLE
                editSearch.isTextInputLayoutFocusedRectEnabled = true
            }
        }
    }

    private fun hideKeywordHistoryView() {
        mBinding.cvSearch.apply{
            if(groupKeywordHistory.isVisible) {
                LogUtil.dLog(LOG_TAG, TAG_CLASS, "hideKeywordHistoryView(...)")
                groupSearch.setBackgroundColor(requireContext().getColor(R.color.ui_background_color))
                KeyboardUtils.hideKeyboard(mBinding.root, requireContext())
                groupKeywordHistory.visibility = View.GONE
                imgClose.visibility = View.GONE
                editSearch.isTextInputLayoutFocusedRectEnabled = false
            }
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
                if(searchHistory.list.isEmpty()) {
                    hideKeywordHistoryView()
                }
                mBinding.cvSearch.recyclerSearchHistory.adapter =
                    SearchKeywordHistoryAdapter(
                        ArrayList(searchHistory.list),
                        { searchHistory ->
                            LogUtil.vLog(LOG_TAG, TAG_CLASS, "Clicked Item : ${searchHistory.kakaoSearchKeyword.id}")
                            mBinding.cvSearch.editSearch.apply {
                                setText(searchHistory.kakaoSearchKeyword.keyword)
                                clearFocus()
                            }
                            hideKeywordHistoryView()
                            searchKeyword(requireContext(), searchHistory.kakaoSearchKeyword.keyword)
                        }, { removeHistory ->
                            removeKeywordHistory(requireContext(), removeHistory.kakaoSearchKeyword.keyword)
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