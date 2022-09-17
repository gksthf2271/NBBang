package com.khs.nbbang.base

import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.DialogFragment
import com.khs.nbbang.R
import com.khs.nbbang.common.IKeyEvent
import com.khs.nbbang.utils.DisplayUtils
import com.khs.nbbang.utils.LogUtil

abstract class BaseDialogFragment(var gDialogType : DIALOG_TYPE) : DialogFragment(), IKeyEvent {
    val TAG_CLASS = this.javaClass.simpleName
    val LOG_TAG = LogUtil.TAG_UI

    val RESULT_SEARCH_FINISH = 1001
    var RESULT_MSG = "MSG"

    enum class DIALOG_TYPE {
        TYPE_SELECT_PEOPLE_FROM_ADD_PLACE_DIALOG,
        TYPE_HISTORY_CHECKER,
        TYPE_COMMON_LOADING,
        TYPE_ADD_KAKAO_FIRENDS,
        TYPE_SHARE_RESULT,
        TYPE_HISTORY_BOTTOM_VIEW,
        TYPE_DATE_PICKER,
        TYPE_MAP,
        NONE
    }

    override fun onResume() {
        super.onResume()
        when (gDialogType) {
            DIALOG_TYPE.NONE,
            DIALOG_TYPE.TYPE_DATE_PICKER,
            DIALOG_TYPE.TYPE_ADD_KAKAO_FIRENDS,
            DIALOG_TYPE.TYPE_SELECT_PEOPLE_FROM_ADD_PLACE_DIALOG,
            DIALOG_TYPE.TYPE_SHARE_RESULT,
            DIALOG_TYPE.TYPE_HISTORY_BOTTOM_VIEW,
            DIALOG_TYPE.TYPE_HISTORY_CHECKER,
            DIALOG_TYPE.TYPE_MAP-> {
                resizeDialog(gDialogType)
                setBackgroundColorDialog()
            }
        }
    }

    private fun setBackgroundColorDialog() {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.layout_common_rounded_deep_black)
//        when (gDialogType) {
//            DIALOG_TYPE.TYPE_SHARE_RESULT,
//            DIALOG_TYPE.TYPE_HISTORY_BOTTOM_VIEW,
//            DIALOG_TYPE.TYPE_ADD_KAKAO_FIRENDS -> {
//                dialog?.window?.setBackgroundDrawableResource(R.drawable.layout_common_rounded_deep_black)
//            }
//            else -> {
//                dialog?.window?.setBackgroundDrawableResource(R.color.blank_deep)
//            }
//        }
    }

    private fun resizeDialog(dialogType : DIALOG_TYPE){
        val size = DisplayUtils().getDisplaySize(requireContext())
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceeHeight = size.y
        when (dialogType) {
            DIALOG_TYPE.TYPE_SELECT_PEOPLE_FROM_ADD_PLACE_DIALOG -> {
                params?.width = (deviceWidth * 0.95).toInt()
                params?.height = (deviceeHeight * 0.65).toInt()
                dialog?.window?.setGravity(Gravity.BOTTOM)
                dialog?.window?.setWindowAnimations(R.style.AnimationPopupStyle)
            }
            DIALOG_TYPE.TYPE_HISTORY_CHECKER -> {
                params?.width = (deviceWidth * 0.95).toInt()
                params?.height = (deviceeHeight * 0.75).toInt()
            }
            DIALOG_TYPE.TYPE_ADD_KAKAO_FIRENDS -> {
                params?.width = (deviceWidth * 0.95).toInt()
                params?.height = (deviceeHeight * 0.90).toInt()
            }
            DIALOG_TYPE.TYPE_MAP,
            DIALOG_TYPE.TYPE_SHARE_RESULT,
            DIALOG_TYPE.TYPE_HISTORY_BOTTOM_VIEW -> {
                params?.width = (deviceWidth * 0.95).toInt()
                params?.height = (deviceeHeight * 0.4).toInt()
                dialog?.window?.setGravity(Gravity.BOTTOM)
                dialog?.window?.setWindowAnimations(R.style.AnimationPopupStyle)
            }
            DIALOG_TYPE.TYPE_DATE_PICKER -> {
                params?.width = (deviceWidth * 0.8).toInt()
                params?.height = (deviceeHeight * 0.3).toInt()
                dialog?.window?.setGravity(Gravity.BOTTOM)
                dialog?.window?.setWindowAnimations(R.style.AnimationPopupStyle)
            }
            DIALOG_TYPE.NONE -> {
                params?.width = (deviceWidth * 1)
                params?.height = (deviceeHeight * 1)
            }
        }
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}