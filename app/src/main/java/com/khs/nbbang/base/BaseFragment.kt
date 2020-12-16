package com.khs.nbbang.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment :Fragment() {
    val TAG = this.javaClass.name

    override fun onStart() {
        super.onStart()
        Log.v(TAG,"onStart(...)")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG,"onViewCreated(...)")
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG,"onResume(...)")
    }

    override fun onDetach() {
        super.onDetach()
        Log.v(TAG,"onDetach(...)")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG,"onDestroy(...)")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v(TAG,"onDestroyView(...)")
    }
}