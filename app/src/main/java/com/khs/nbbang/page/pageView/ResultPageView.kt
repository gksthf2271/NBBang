package com.khs.nbbang.page.pageView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseView
import com.khs.nbbang.databinding.CviewResultPageBinding
import com.khs.nbbang.page.viewModel.PageViewModel

class ResultPageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), BaseView {
    val mBinding : CviewResultPageBinding
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = DataBindingUtil.inflate(inflater, R.layout.cview_result_page, this, true)
    }

    override fun showError(error: Int) {
        TODO("Not yet implemented")
    }

    override fun setViewModel(viewModel: PageViewModel, owner: Fragment) {
        mBinding.viewModel = viewModel
        mBinding.lifecycleOwner = owner
    }
}