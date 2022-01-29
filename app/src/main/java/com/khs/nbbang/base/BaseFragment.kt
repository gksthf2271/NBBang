package com.khs.nbbang.base

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.khs.nbbang.common.IKeyEvent
import com.khs.nbbang.common.LoadingDialog

open abstract class BaseFragment :Fragment(), IKeyEvent{
    val TAG_CLASS = this.javaClass.simpleName
    lateinit var gLoadingView: Dialog

    override fun onStart() {
        super.onStart()
        Log.v(TAG_CLASS,"onStart(...)")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG_CLASS,"onViewCreated(...)")
        makeCustomLoadingView()?.let {
            gLoadingView = it
            return
        }
        gLoadingView = makeCommonLoadingView()
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG_CLASS,"onResume(...)")
    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG_CLASS,"onPause(...)")
    }

    override fun onDetach() {
        super.onDetach()
        Log.v(TAG_CLASS,"onDetach(...)")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG_CLASS,"onDestroy(...)")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v(TAG_CLASS,"onDestroyView(...)")
    }

    fun isShownLoadingView() : Boolean {
        return (gLoadingView as? LoadingDialog)?.let {
            return@let it.isShowing
        } ?: false
    }

    private fun makeCommonLoadingView() : LoadingDialog {
        Log.v(TAG_CLASS,"makeCommonLoadingView(...)")
        return LoadingDialog(requireContext())
    }

    protected abstract fun makeCustomLoadingView() : Dialog?

    protected fun showLoadingView() {
        Log.v(TAG_CLASS,"showLoadingView(...)")
        (gLoadingView as? LoadingDialog)?.let {
            if (!it.isShowing)
                it.show()
        }
    }

    protected fun hideLoadingView() {
        Log.v(TAG_CLASS,"hideLoadingView(...)")
        (gLoadingView as? LoadingDialog)?.let {
            if (it.isShowing)
                it.hide()
        }
    }
}