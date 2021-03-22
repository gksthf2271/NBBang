package com.khs.nbbang.base

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.khs.nbbang.common.LoadingDialog

open abstract class BaseFragment :Fragment() {
    val TAG = this.javaClass.simpleName
    lateinit var gLoadingView: Dialog

    override fun onStart() {
        super.onStart()
        Log.v(TAG,"onStart(...)")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG,"onViewCreated(...)")
        makeCustomLoadingView()?.let {
            gLoadingView = it!!
            return
        }
        gLoadingView = makeCommonLoadingView()
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG,"onResume(...)")
    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG,"onPause(...)")
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

    fun isShownLoadingView() : Boolean {
        return (gLoadingView as? LoadingDialog).let {
            return@let it!!.isShowing
        }
    }

    private fun makeCommonLoadingView() : LoadingDialog {
        Log.v(TAG,"makeCommonLoadingView(...)")
        return LoadingDialog(requireContext())
    }

    protected abstract fun makeCustomLoadingView() : Dialog?

    protected fun showLoadingView() {
        (gLoadingView as? LoadingDialog).let {
            if (!it!!.isShowing)
                it!!.show()
        }
    }

    protected fun hideLoadingView() {
        (gLoadingView as? LoadingDialog).let {
            if (it!!.isShowing)
                it!!.hide()
        }
    }
}