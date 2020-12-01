package com.khs.nbbang.page.pageView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.khs.nbbang.R
import com.khs.nbbang.page.viewModel.PageViewModel

class PeopleCountView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var mBinding : com.khs.nbbang.databinding.CviewSelectCountBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = DataBindingUtil.inflate(inflater, R.layout.cview_select_count, this, true)
    }

    fun setViewModel(viewModel: PageViewModel, fragment: Fragment) {
        mBinding.viewModel = viewModel
        mBinding.lifecycleOwner = fragment
    }
}