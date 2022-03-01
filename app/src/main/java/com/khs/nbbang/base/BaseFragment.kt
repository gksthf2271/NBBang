package com.khs.nbbang.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.khs.nbbang.common.IKeyEvent
import com.khs.nbbang.common.LoadingDialog
import com.khs.nbbang.utils.LogUtil

open abstract class BaseFragment :Fragment(), IKeyEvent{
    val TAG_CLASS = this.javaClass.simpleName
    open val LOG_TAG = LogUtil.TAG_UI
    lateinit var gLoadingView: Dialog

    override fun onStart() {
        super.onStart()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onStart(...)")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onViewCreated(...)")
        makeCustomLoadingView()?.let {
            gLoadingView = it
            return
        }
        gLoadingView = makeCommonLoadingView()
    }

    override fun onResume() {
        super.onResume()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onResume(...)")
    }

    override fun onPause() {
        super.onPause()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onPause(...)")
    }

    override fun onDetach() {
        super.onDetach()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onDetach(...)")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onDestroy(...)")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onDestroyView(...)")
    }

    fun isShownLoadingView() : Boolean {
        return (gLoadingView as? LoadingDialog)?.let {
            return@let it.isShowing
        } ?: false
    }

    private fun makeCommonLoadingView() : LoadingDialog {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "makeCommonLoadingView(...)")
        return LoadingDialog(requireContext())
    }

    protected abstract fun makeCustomLoadingView() : Dialog?

    protected fun showLoadingView() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "showLoadingView(...)")
        (gLoadingView as? LoadingDialog)?.let {
            if (!it.isShowing && isAdded && requireActivity().isActivityTransitionRunning)
                it.show()
        }
    }

    protected fun hideLoadingView() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "hideLoadingView(...)")
        (gLoadingView as? LoadingDialog)?.let {
            if (it.isShowing)
                it.hide()
        }
    }
}