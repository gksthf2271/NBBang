package com.khs.nbbang.login

import android.util.Log
import androidx.fragment.app.Fragment
import com.khs.nbbang.base.BaseView
import com.khs.nbbang.page.viewModel.PageViewModel

class LoginView : BaseView{
    val TAG = this.javaClass.simpleName

    override fun showError(error: Int) {
        Log.v(TAG,"showError(...)")
    }

    override fun setViewModel(viewModel: PageViewModel, owner: Fragment) {
    }
}