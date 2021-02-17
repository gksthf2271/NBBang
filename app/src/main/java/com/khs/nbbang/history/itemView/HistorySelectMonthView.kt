package com.khs.nbbang.history.itemView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.databinding.CviewSelectMonthBinding
import com.khs.nbbang.history.HistoryViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//class HistorySelectMonthView @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
//) : ConstraintLayout(context, attrs, defStyleAttr), KoinComponent {
//    val TAG = this.javaClass.name
//    var mBinding: CviewSelectMonthBinding
//    val mViewModel : HistoryViewModel by inject()
//
//    init {
//        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        mBinding = CviewSelectMonthBinding.inflate(inflater, this,true)
//    }
//
//    fun setMonth(month: Int) {
//        mBinding.txtMonth.text = "$month 월"
//    }
//}