package com.khs.nbbang.page

import android.util.Log
import androidx.fragment.app.Fragment
import com.khs.nbbang.base.BaseView
import com.khs.nbbang.page.viewModel.PageViewModel

class SelectCategoryView : BaseView {
    val TAG = this.javaClass.name

    override fun showError(error: Int) {
        Log.v(TAG,"showError(...)")

    }

    override fun setViewModel(viewModel: PageViewModel, owner: Fragment) {
        TODO("Not yet implemented")
    }
}