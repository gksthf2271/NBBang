package com.khs.nbbang.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.databinding.CviewSearchBinding
import com.khs.nbbang.utils.KeyboardUtils
import com.khs.nbbang.utils.KeyboardVisibilityUtils
import com.khs.nbbang.utils.LogUtil

class SearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    val TAG_CLASS = this.javaClass.simpleName
    val LOG_TAG = LogUtil.TAG_UI
    var mBinding: CviewSearchBinding
    private lateinit var mKeyboardVisibilityUtils: KeyboardVisibilityUtils
    private lateinit var mKakaoViewModel : KakaoLocalViewModel

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewSearchBinding.inflate(inflater, this, true)
    }

    fun initView(activity: FragmentActivity, viewModel: KakaoLocalViewModel) {
        mKakaoViewModel = viewModel
        mBinding.apply {
            groupKeywordHistory.setBackgroundColor(context.getColor(R.color.search_history_backgrond))
            editSearch.apply {
                setOnEditorActionListener { view, actionId, keyEvent ->
                    LogUtil.dLog(LOG_TAG, TAG_CLASS, "OnEditorActionListener(...) keyEvent : ${keyEvent}")
                    if (view.hasFocus() && actionId == EditorInfo.IME_ACTION_SEARCH) {
                        hideKeywordHistoryView()
                        val searchText = (view as EditText).text.toString()
                        if (searchText.isNullOrEmpty()) {
                            Toast.makeText(context, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
                            return@setOnEditorActionListener false
                        }
                        mKakaoViewModel.searchKeyword(context, searchText)
                        view.clearFocus()
                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener false
                }
                mKeyboardVisibilityUtils = KeyboardVisibilityUtils(activity.window,
                    onShowKeyboard = { keyboardHeight ->
                        showKeywordHistoryView()
                    },
                    onHideKeyboard = {
                        hideKeywordHistoryView()
                    })

                txtRemoveAll.setOnClickListener {
                    mKakaoViewModel.removeAllKeywordHistory(context)
                }
            }
            imgClose.setOnClickListener {
                hideKeywordHistoryView()
            }
            recyclerSearchHistory.apply {
                addItemDecoration(HistoryItemDecoration(20))
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    fun setupSearchHistoryList(searchHistory: GetSearchAllResult) {
        mBinding.recyclerSearchHistory.adapter =
            SearchKeywordHistoryAdapter(
                ArrayList(searchHistory.list),
                { searchHistory ->
                    LogUtil.vLog(LOG_TAG, TAG_CLASS, "Clicked Item : ${searchHistory.kakaoSearchKeyword.id}")
                    mBinding.editSearch.apply {
                        setText(searchHistory.kakaoSearchKeyword.keyword)
                        clearFocus()
                    }
                    hideKeywordHistoryView()
                    mKakaoViewModel.searchKeyword(context, searchHistory.kakaoSearchKeyword.keyword)
                }, { removeHistory ->
                    mKakaoViewModel.removeKeywordHistory(context, removeHistory.kakaoSearchKeyword.keyword)
                })
    }

    private fun showKeywordHistoryView() {
        mKakaoViewModel.showSearchHistory(context)
        mBinding.apply{
            if(!groupKeywordHistory.isVisible) {
                LogUtil.dLog(LOG_TAG, TAG_CLASS, "showKeywordHistoryView(...)")
                groupSearch.setBackgroundColor(context.getColor(R.color.logo_background_color))
                groupKeywordHistory.visibility = View.VISIBLE
                imgClose.visibility = View.VISIBLE
                editSearch.isTextInputLayoutFocusedRectEnabled = true
            }
        }
    }

    private fun isShownKeywordHistoryView() : Boolean {
        mBinding.apply{
            return groupKeywordHistory.isVisible
        }
    }

    private fun hideKeywordHistoryView() {
        mBinding.apply{
            if(groupKeywordHistory.isVisible) {
                LogUtil.dLog(LOG_TAG, TAG_CLASS, "hideKeywordHistoryView(...)")
                groupSearch.setBackgroundColor(context.getColor(R.color.ui_background_color))
                KeyboardUtils.hideKeyboard(mBinding.root, context)
                groupKeywordHistory.visibility = View.GONE
                imgClose.visibility = View.GONE
                editSearch.isTextInputLayoutFocusedRectEnabled = false
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mKeyboardVisibilityUtils.detachKeyboardListeners()
    }
}