package com.khs.nbbang.base

import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.khs.nbbang.R
import com.khs.nbbang.utils.DisplayUtils

open class BaseDialogFragment(var gDialogType : DIALOG_TYPE) : DialogFragment() {
    enum class DIALOG_TYPE {
        TYPE_SELECT_PEOPLE_FROM_ADD_PLACE_DIALOG,
        TYPE_HISTORY_CHECKER,
        TYPE_COMMON_LOADING,
        NONE
    }

    override fun onResume() {
        super.onResume()
        when (gDialogType) {
            DIALOG_TYPE.TYPE_SELECT_PEOPLE_FROM_ADD_PLACE_DIALOG,
            DIALOG_TYPE.TYPE_HISTORY_CHECKER -> {
                resizeDialog(gDialogType)
                setBackgroundColorDialog()
            }
        }
    }

    private fun setBackgroundColorDialog() {
        dialog?.window?.setBackgroundDrawableResource(R.color.blank)
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
            DIALOG_TYPE.NONE -> {
                params?.width = (deviceWidth * 1).toInt()
                params?.height = (deviceeHeight * 1).toInt()
            }
        }
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}