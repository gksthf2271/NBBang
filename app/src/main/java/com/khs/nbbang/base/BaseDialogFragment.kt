package com.khs.nbbang.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.khs.nbbang.utils.DisplayUtils

open class BaseDialogFragment : DialogFragment() {
    enum class DIALOG_TYPE {
        TYPE_SELECT_PEOPLE_FROM_ADD_PLACE_DIALOG,
        TYPE_HISTORY_CHECKER,
        NONE
    }

    var CURRENT_DIALOG_TYPE = DIALOG_TYPE.NONE

    override fun onResume() {
        super.onResume()
        resizeDialog(CURRENT_DIALOG_TYPE)
        setBackgroundColorDialog()
    }

    fun setBackgroundColorDialog() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun resizeDialog(dialogType : DIALOG_TYPE){
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
                params?.height = (deviceeHeight * 0.40).toInt()
            }
            DIALOG_TYPE.NONE -> {
                params?.width = (deviceWidth * 1).toInt()
                params?.height = (deviceeHeight * 1).toInt()
            }
        }
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}