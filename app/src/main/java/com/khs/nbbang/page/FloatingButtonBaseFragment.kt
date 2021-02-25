package com.khs.nbbang.page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment

abstract class FloatingButtonBaseFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View =
            inflater.inflate(R.layout.fragment_floating_btn_base, container, false)
        fillFragmentContainer(makeContentsFragment())
        return v
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    protected open fun fillFragmentContainer(fragment: Fragment?) {
        Log.d(TAG, "fill Fragment Container with fragment:$fragment")
        if (fragment != null && isAdded) {
            val ft =
                childFragmentManager.beginTransaction()
            ft.replace(R.id.contents_container, fragment)
            ft.commitNowAllowingStateLoss()
        } else {
            Log.d(TAG, "contentsFragment is null!!!")
        }
    }

    protected abstract fun makeContentsFragment(): Fragment?

    protected abstract fun init()
}