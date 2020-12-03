package com.khs.nbbang.base

import androidx.fragment.app.Fragment
import com.khs.nbbang.page.viewModel.PageViewModel

interface BaseView {
    fun showError(error: Int)
    fun setViewModel(viewModel: PageViewModel, owner: Fragment)
}