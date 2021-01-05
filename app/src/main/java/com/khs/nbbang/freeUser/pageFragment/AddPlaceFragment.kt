package com.khs.nbbang.freeUser.pageFragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddPlaceBinding
import com.khs.nbbang.freeUser.`interface`.TextWatcherAdapter
import com.khs.nbbang.freeUser.viewModel.PageViewModel
import kotlinx.android.synthetic.main.cview_add_edit_place.view.*
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
        mBinding.viewModel = ViewModelProvider(
            requireActivity(),
            PageViewModel.PageViewModelFactory(
                requireActivity().supportFragmentManager,
                requireActivity().application
            )
        ).get(PageViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        initView()

    }

    fun initView() {
        val rootView = mBinding.layoutGroup
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val addView: ConstraintLayout =
            inflater.inflate(R.layout.cview_add_edit_place, rootView, false) as ConstraintLayout
        rootView.addView(addView)

        rootView.btn_add.setOnClickListener {
            val infoView: ConstraintLayout =
                inflater.inflate(R.layout.cview_edit_place, rootView, false) as ConstraintLayout
            infoView.btn_join.setOnClickListener {
                showSelectPeopleDialog(infoView.tag.toString())
            }

            rootView.addView(infoView.apply {
                var placeIndex = rootView.childCount - 1
                infoView.tag = placeIndex
                infoView.txt_index.text = "$placeIndex 차"

                infoView.edit_title.addTextChangedListener(object : TextWatcherAdapter {
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        super.onTextChanged(s, start, before, count)
                        mBinding.viewModel!!._selectedPeopleMap.value!!.get(infoView.tag as Int)
                        //todo textWatcher <-> viewModel 상호작용 구현
                    }
                })
                mBinding.viewModel!!._placeCount.value = placeIndex
            },rootView.childCount - 1)
        }
        mBinding.viewModel.let {
            it!!._placeCount.observe(requireActivity(), Observer {
                if (it == 0) return@Observer
//                mBinding.viewModel!!.createPlaceHashMap()
        })
        }
    }

    fun showSelectPeopleDialog(tag:String){
        Log.v(TAG,"showSelectPeopleDialog(...)")
        var selectPeopleDialog = SelectPeopleDialogFragment.getInstance()
        selectPeopleDialog.show(requireActivity().supportFragmentManager, tag)
    }
}