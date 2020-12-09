package com.khs.nbbang.freeUser.pageFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddPlaceBinding
import com.khs.nbbang.databinding.FragmentResultPageBinding
import kotlinx.android.synthetic.main.cview_edit_place.view.*

class AddPlaceFragment : BaseFragment() {
    lateinit var mBinding : FragmentAddPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    fun initView() {
        mBinding.btnAdd.setOnClickListener {
            val rootView = mBinding.layoutGroup
            val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val infoView: ConstraintLayout =
                inflater.inflate(R.layout.cview_edit_place, rootView, false) as ConstraintLayout
            rootView.addView(infoView)
            infoView.txt_index.text = "${rootView.childCount} 차"
        }
    }
}