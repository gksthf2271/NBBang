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
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
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
import com.khs.nbbang.page.ItemObj.NNBObj
import kotlinx.android.synthetic.main.cview_add_edit_place.view.*
import kotlinx.android.synthetic.main.cview_edit_place.view.*

class AddPlaceFragment : BaseFragment() {
    lateinit var mBinding : FragmentAddPlaceBinding
    val TYPE_EDIT_PLACE_NAME :String = "TYPE_EDIT_PLACE_NAME"
    val TYPE_EDIT_PRICE :String = "TYPE_EDIT_PRICE"

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
                var placeIndex = rootView.childCount
                infoView.tag = placeIndex
                infoView.txt_index.text = "$placeIndex 차"

                infoView.edit_title.addTextChangedListener(getTextWatcher(TYPE_EDIT_PLACE_NAME, infoView.tag as Int))
                infoView.edit_price.addTextChangedListener(getTextWatcher(TYPE_EDIT_PRICE, infoView.tag as Int))

                mBinding.viewModel.let {
                    it!!._placeCount.value = placeIndex

                    it!!._selectedPeopleMap.observe(requireActivity(), Observer {
                        it.get(infoView.tag as Int).let {
                            it?.let {
                                if (it!!.mPeopleList.isEmpty()) {
                                    hideAddedPeopleView(infoView)
                                } else {
                                    showAddedPeopleView(infoView, it!!)
                                }
                            }
                        }
                    })
                }
            },rootView.childCount - 1)
        }
        mBinding.viewModel.let {
            it!!._placeCount.observe(requireActivity(), Observer {
                if (it == 0) return@Observer
            })
        }
    }

    fun showSelectPeopleDialog(tag:String){
        Log.v(TAG,"showSelectPeopleDialog(...)")
        var selectPeopleDialog = SelectPeopleDialogFragment.getInstance()
        selectPeopleDialog.show(requireActivity().supportFragmentManager, tag)
    }

    fun showAddedPeopleView(view : ConstraintLayout, nnbObj: NNBObj) {
        Log.v(TAG,"showAddedPeopleView(...), ${view.txt_index}")
        if (view.layout_group_added_people.isVisible) return
        view.txt_added_people.apply {
            var peopleNameString = ""
            for(peopleName in nnbObj.mPeopleList) {
                peopleNameString =  peopleNameString + " | " + peopleName
            }
            this!!.text = peopleNameString
        }
        view.layout_group_added_people.visibility = View.VISIBLE
    }

    fun hideAddedPeopleView(view : ConstraintLayout) {
        Log.v(TAG,"hideAddedPeopleView(...), ${view.txt_index}")
        view.layout_group_added_people.visibility = View.GONE
    }

    fun getTextWatcher(viewType : String, viewTag:Int) : TextWatcher {
        val textWatcher  = object : TextWatcherAdapter{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                if (TYPE_EDIT_PLACE_NAME.equals(viewType)) {
                    mBinding.viewModel!!._selectedPeopleMap.value!!.get(viewTag)
                        ?.let {
                            it!!.mPlaceName = s.toString()
                        }
                } else if (TYPE_EDIT_PRICE.equals(viewType)) {
                    mBinding.viewModel!!._selectedPeopleMap.value!!.get(viewTag)
                        ?.let {
                            it!!.mPrice = s.toString()
                        }
                }
            }

        }
        return textWatcher
    }
}