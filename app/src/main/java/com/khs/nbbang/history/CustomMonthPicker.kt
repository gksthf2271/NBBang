package com.khs.nbbang.history

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.CviewDatePickerBinding
import com.khs.nbbang.utils.DateUtils
import com.khs.nbbang.utils.LogUtil

class CustomMonthPicker(
    var currentYear: Int? = null,
    var currentMonth: Int? = null,
    val callback: (Int, Int) -> Unit
) : BaseDialogFragment(DIALOG_TYPE.TYPE_DATE_PICKER) {
    lateinit var mBinding: CviewDatePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = CviewDatePickerBinding.inflate(inflater, container, true)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        val year = mBinding.yearpickerDatepicker
        val month = mBinding.monthpickerDatepicker

        //  순환 안되게 막기
        year.wrapSelectorWheel = false
        month.wrapSelectorWheel = false

        //  editText 설정 해제
        year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        //  최소값 설정
        year.minValue = 2010
        month.minValue = 1

        //  최대값 설정
        year.maxValue = 2030
        month.maxValue = 12

        year.value = currentYear ?: DateUtils.currentYear()
        month.value = currentMonth ?: DateUtils.currentMonth()

        mBinding.btnCancel.setOnClickListener {
            dismiss()
        }

        mBinding.btnSave.setOnClickListener {
            LogUtil.dLog(LOG_TAG, TAG_CLASS, "DatePickerDialog > year : ${year.value}, month : ${month.value}")
            callback(year.value, month.value)
            dismiss()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (this.isAdded) {
            dismiss()
            return true
        }
        return false
    }
}