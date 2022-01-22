package com.khs.nbbang.local.search

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khs.nbbang.base.BaseFragment

class SearchFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun makeCustomLoadingView(): Dialog? {
        TODO("Not yet implemented")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }
}