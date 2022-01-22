package com.khs.nbbang.local.search

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentSearchHomeBinding
import com.khs.nbbang.search.KakaoLocalViewModel
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
        mBinding.cvSearch.editSearch.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
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
    }

    private fun addObserver() {
        mKakaoViewModel.mSearchResult.observe(requireActivity(), Observer { searchResult ->
            Log.i(TAG,"search result -> $searchResult")
        })
    }

    override fun makeCustomLoadingView(): Dialog? {
        return null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}