package com.khs.nbbang.page.pageFragment

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddPlaceBinding
import com.khs.nbbang.page.adapter.TextWatcherAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.page.ItemObj.NNBObj
import com.khs.nbbang.utils.StringUtils
import kotlinx.android.synthetic.main.cview_add_edit_place.view.*
import kotlinx.android.synthetic.main.cview_edit_place.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class AddPlaceFragment : BaseFragment() {
    lateinit var mBinding : FragmentAddPlaceBinding

    val TYPE_EDIT_PLACE_NAME :String = "TYPE_EDIT_PLACE_NAME"
    val TYPE_EDIT_PRICE :String = "TYPE_EDIT_PRICE"
    val mViewModel : PageViewModel by sharedViewModel()

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
        mBinding.viewModel = mViewModel

        initView()
        addObserver()
    }


    private fun initView() {
        val rootView = mBinding.layoutGroup
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val addView: ConstraintLayout =
            inflater.inflate(R.layout.cview_add_edit_place, rootView, false) as ConstraintLayout
        rootView.addView(addView)

        rootView.btn_add.setOnClickListener {
            rootView.addView(createPlaceInfoView(rootView, inflater), rootView.childCount - 1)
        }

        mBinding.viewModel.let {
            it!!._placeCount.observe(requireActivity(), Observer {
                if (it == 0) return@Observer
            })
        }
    }

    private fun addObserver() {
        mBinding.viewModel.let {
            val viewModel = mBinding.viewModel!!
            it!!._NNBLiveData.observe(requireActivity(), Observer {
                Log.v(TAG, "_NNBLiveData, Observer(...) : $it")
                viewModel.clearSelectedPeople()
            })
        }
    }

    private fun createPlaceInfoView(rootView: LinearLayout, inflater: LayoutInflater): ConstraintLayout {
        val infoView: ConstraintLayout =
            inflater.inflate(R.layout.cview_edit_place, rootView, false) as ConstraintLayout

        var placeIndex = rootView.childCount

        infoView.apply {
            this.btn_join.setOnClickListener {
                showSelectPeopleDialog(infoView.tag.toString())
            }
            this.tag = placeIndex
            this.txt_index.text = "$placeIndex 차"
            this.edit_title.addTextChangedListener(getTextWatcher(TYPE_EDIT_PLACE_NAME, this.tag as Int))
            this.edit_price.addTextChangedListener(getTextWatcher(TYPE_EDIT_PRICE, this.tag as Int))
        }

        mBinding.viewModel.let {
            it!!._placeCount.value = placeIndex

            it!!._selectedPeopleMap.observe(requireActivity(), Observer {
                it.get(infoView.tag as Int) ?: return@Observer
                Log.v(TAG, "_selectedPeopleMap, Observer(...) : $it")
                if (it!!.get(infoView.tag as Int)!!.mPeopleList.isEmpty()) {
                    hideAddedPeopleView(infoView)
                } else {
                    showAddedPeopleView(infoView, it!!.get(infoView.tag as Int)!!)
                }
            })

        }
        return infoView
    }

    private fun showSelectPeopleDialog(tag:String){
        Log.v(TAG,"showSelectPeopleDialog(...)")
        var selectPeopleDialog = SelectPeopleDialogFragment.getInstance()
        selectPeopleDialog.show(requireActivity().supportFragmentManager, tag)
    }

    private fun showAddedPeopleView(view : ConstraintLayout, nnbObj: NNBObj) {
        Log.v(TAG,"showAddedPeopleView(...), ${view.txt_index.text}")
        view.txt_added_people.apply {
            this!!.text = StringUtils().getPeopleList(nnbObj.mPeopleList)
        }
        view.layout_group_added_people.visibility = View.VISIBLE
    }

    private fun hideAddedPeopleView(view : ConstraintLayout) {
        Log.v(TAG,"hideAddedPeopleView(...), ${view.txt_index}")
        view.layout_group_added_people.visibility = View.GONE
    }

    private fun getTextWatcher(viewType : String, placeId:Int) : TextWatcher {
        return object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                if (TYPE_EDIT_PLACE_NAME.equals(viewType)) {
                    mBinding.viewModel!!.savePlaceName(placeId, s.toString())
                } else if (TYPE_EDIT_PRICE.equals(viewType)) {
                    mBinding.viewModel!!.savePrice(placeId, s.toString())
                }
            }
        }
    }
}