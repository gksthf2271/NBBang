package com.khs.nbbang.page.pageView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.AdapterView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseView
import com.khs.nbbang.databinding.CviewAddPeopleBinding
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.viewModel.PageViewModel
import kotlinx.android.synthetic.main.cview_add_people.view.*


class AddPeopleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), BaseView{
    val TAG = this.javaClass.name
    val mBinding : CviewAddPeopleBinding
    val mGridViewAdapter : AddPeopleViewAdapter

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = DataBindingUtil.inflate(inflater, R.layout.cview_add_people, this, true)
        mGridViewAdapter = AddPeopleViewAdapter(context, mutableListOf())
        initView()
    }

    fun initView() {
        view_grid.adapter = mGridViewAdapter
        updateCircle()
        view_grid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Log.v(TAG, "TEST, onItemClicked, $position")
            if (position == 0) mGridViewAdapter.addItem(
                mGridViewAdapter.count,
                People(mGridViewAdapter.count, "")
            )
        }
    }

    fun updateCircle() {
        updateCircle(2)
    }

    fun updateCircle(count: Int) {
        mGridViewAdapter.mItemList.clear()
        var dummyPeople = People(0," + ")
        mGridViewAdapter.addItem(dummyPeople)
        mGridViewAdapter.addItem(People(1, "김한솔"))
        for (index in 2 until count) {
            mGridViewAdapter.addItem(index, People(index, ""))
        }
    }

    override fun showError(error: Int) {
        TODO("Not yet implemented")
    }

    override fun setViewModel(viewModel: PageViewModel, owner: Fragment) {
        mBinding.viewModel = viewModel
        mBinding.lifecycleOwner = owner
    }

}