package com.khs.nbbang.kakaoFriends

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment

class FavoriteFriendsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_management, container, false)
    }

    override fun makeCustomLoadingView(): Dialog? {
        TODO("Not yet implemented")
        return null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        TODO("Not yet implemented")
        return false
    }
}