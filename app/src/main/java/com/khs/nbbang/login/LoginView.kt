package com.khs.nbbang.login

import android.util.Log
import androidx.fragment.app.Fragment
import com.khs.nbbang.base.BaseView
import com.khs.nbbang.freeUser.viewModel.PageViewModel

class LoginView : BaseView{
    val TAG = this.javaClass.name

    override fun showError(error: Int) {
        Log.v(TAG,"showError(...)")
    }

    override fun setViewModel(viewModel: PageViewModel, owner: Fragment) {
        TODO("Not yet implemented")
    }
}