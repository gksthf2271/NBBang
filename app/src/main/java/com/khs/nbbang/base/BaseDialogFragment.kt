package com.khs.nbbang.base

import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.khs.nbbang.R
import com.khs.nbbang.common.IKeyEvent
import com.khs.nbbang.utils.DisplayUtils

open abstract class BaseDialogFragment(var gDialogType : DIALOG_TYPE) : DialogFragment(), IKeyEvent {
    val TAG = this.javaClass.simpleName
    enum class DIALOG_TYPE {
        TYPE_SELECT_PEOPLE_FROM_ADD_PLACE_DIALOG,
        TYPE_HISTORY_CHECKER,
        TYPE_COMMON_LOADING,
        TYPE_ADD_KAKAO_FIRENDS,
        TYPE_HISTORY_BOTTOM_VIEW,
        NONE
    }

    override fun onResume() {
        super.onResume()
        when (gDialogType) {
            DIALOG_TYPE.NONE,
            DIALOG_TYPE.TYPE_ADD_KAKAO_FIRENDS,
            DIALOG_TYPE.TYPE_SELECT_PEOPLE_FROM_ADD_PLACE_DIALOG,
            DIALOG_TYPE.TYPE_HISTORY_CHECKER -> {
                resizeDialog(gDialogType)
                setBackgroundColorDialog()
            }
        }
    }

    private fun setBackgroundColorDialog() {
        when (gDialogType) {
            DIALOG_TYPE.TYPE_ADD_KAKAO_FIRENDS -> {
                dialog?.window?.setBackgroundDrawableResource(R.color.blank_deep)
            }
            else -> {
                dialog?.window?.setBackgroundDrawableResource(R.color.blank)
            }
        }
    }

    private fun resizeDialog(dialogType : DIALOG_TYPE){
        val size = DisplayUtils().getDisplaySize(requireContext())
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceeHeight = size.y
        when (dialogType) {
            DIALOG_TYPE.TYPE_SELECT_PEOPLE_FROM_ADD_PLACE_DIALOG -> {
                params?.width = (deviceWidth * 0.95).toInt()
                params?.height = (deviceeHeight * 0.75).toInt()
            }
            DIALOG_TYPE.TYPE_HISTORY_CHECKER -> {
                params?.width = (deviceWidth * 0.95).toInt()
                params?.height = (deviceeHeight * 0.75).toInt()
            }
            DIALOG_TYPE.TYPE_ADD_KAKAO_FIRENDS -> {
                params?.width = (deviceWidth * 0.95).toInt()
                params?.height = (deviceeHeight * 0.90).toInt()
            }
            DIALOG_TYPE.TYPE_HISTORY_BOTTOM_VIEW -> {
                params?.width = (deviceWidth * 0.95).toInt()
                params?.height = (deviceeHeight * 0.4).toInt()
            }
            DIALOG_TYPE.NONE -> {
                params?.width = (deviceWidth * 1)
                params?.height = (deviceeHeight * 1)
            }
        }
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}