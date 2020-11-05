package com.khs.nbbang.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment

class FreeUserFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view : View = inflater.inflate(R.layout.fragment_select_page, container, false)
        return view
    }


}