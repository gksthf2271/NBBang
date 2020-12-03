package com.khs.nbbang.page.pageView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseView
import com.khs.nbbang.page.viewModel.PageViewModel

class PeopleCountView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), BaseView {
    var mBinding : com.khs.nbbang.databinding.CviewSelectCountBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = DataBindingUtil.inflate(inflater, R.layout.cview_select_count, this, true)
    }

    override fun showError(error: Int) {
        TODO("Not yet implemented")
    }

    override fun setViewModel(viewModel: PageViewModel, owner: Fragment) {
        mBinding.viewModel = viewModel
        mBinding.lifecycleOwner = owner
    }
}