package com.khs.nbbang.page.dutchPayPageFragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddPlaceBinding
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.page.adapter.TextWatcherAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.utils.KeyboardVisibilityUtils
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils
import kotlinx.android.synthetic.main.cview_edit_place.view.*
import kotlinx.android.synthetic.main.fragment_add_place.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class AddPlaceFragment : BaseFragment() {
    lateinit var mBinding: FragmentAddPlaceBinding

    val mViewModel: PageViewModel by sharedViewModel()
    private val TYPE_EDIT_PLACE_NAME: String = "TYPE_EDIT_PLACE_NAME"
    private val TYPE_EDIT_PRICE: String = "TYPE_EDIT_PRICE"
    private lateinit var gKeyboardVisibilityUtils: KeyboardVisibilityUtils

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

    override fun onPause() {
        super.onPause()
        mViewModel.selectPlace(-1)
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }

    fun initView() {
        val rootView = mBinding.layoutGroup
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        gKeyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = { keyboardHeight ->
                mBinding.layoutScroll.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })
        rootView.addView(createPlaceInfoView(rootView, inflater), rootView.childCount)
        mBinding.btnAdd.setOnClickListener {
            rootView.addView(createPlaceInfoView(rootView, inflater), rootView.childCount)
        }
    }

    private fun addObserver() {
        mViewModel.mNBBLiveData.observe(requireActivity(), Observer {
            Log.v(TAG, "mNBBLiveData, Observer(...) : $it")
            mViewModel.clearSelectedPeople()
        })
    }

    private fun showSelectPeopleDialog(tag: String) {
        Log.v(TAG, "showSelectPeopleDialog(...)")
        val selectPeopleDialog = SelectPeopleDialogFragment.getInstance()
        when {
            selectPeopleDialog.isAdded -> {
                return
            }
            else -> {
                selectPeopleDialog.show(requireActivity().supportFragmentManager, tag)
            }
        }
    }

    private fun createPlaceInfoView(
        rootView: LinearLayout,
        inflater: LayoutInflater
    ): ConstraintLayout {
        val infoView: ConstraintLayout =
            inflater.inflate(R.layout.cview_edit_place, rootView, false) as ConstraintLayout

        val placeIndex = rootView.childCount + 1

        infoView.apply {
            this.btn_join.setOnClickListener {
                showSelectPeopleDialog(infoView.tag.toString())
            }
            this.tag = placeIndex
            this.txt_index.text = "$placeIndex 차"
            this.edit_title.addTextChangedListener(
                getTextWatcher(
                    edit_title,
                    TYPE_EDIT_PLACE_NAME,
                    this.tag as Int
                )
            )
            this.edit_price.addTextChangedListener(
                getTextWatcher(
                    edit_price,
                    TYPE_EDIT_PRICE,
                    this.tag as Int
                )
            )

            setOnClickListener {
                Log.v(TAG, "Clicked ${this.tag}차 PlaceInfoView!")
            }
        }

        mBinding.viewModel?.let {
            it.updateJoinPlaceCount(placeIndex)

            it.mSelectedPeopleMap.observe(requireActivity(), Observer {
                it[infoView.tag as Int] ?: return@Observer
                Log.v(TAG, "_selectedPeopleMap, Observer(...) : ${it.count()}")
                if (it[infoView.tag as Int]!!.mMemberList.isEmpty()) {
                    hideAddedPeopleView(infoView)
                } else {
                    showAddedPeopleView(infoView, it[infoView.tag as Int]!!)
                }
            })
        }
        return infoView
    }

    private fun showAddedPeopleView(view: ConstraintLayout, nbb: NBB) {
        Log.v(TAG, "showAddedPeopleView(...), ${view.txt_index.text}")
        view.txt_added_people.text = StringUtils().getPeopleList(nbb.mMemberList)
        Log.v(TAG,"addedMember : ${view.txt_added_people.text}")
        view.txt_added_people.visibility = View.VISIBLE
    }

    private fun hideAddedPeopleView(view: ConstraintLayout) {
        Log.v(TAG, "hideAddedPeopleView(...), ${view.txt_index}")
        view.txt_added_people.visibility = View.GONE
    }

    private fun getTextWatcher(view: EditText, viewType: String, placeId: Int): TextWatcher {
        return object : TextWatcherAdapter() {
            var pointNumStr = ""
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                if (TYPE_EDIT_PLACE_NAME.equals(viewType)) {
                    mBinding.viewModel!!.savePlaceName(placeId, s.toString())
                } else if (TYPE_EDIT_PRICE.equals(viewType)) {
                    mBinding.viewModel!!.savePrice(placeId, s.toString())
                    if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(pointNumStr)) {
                        try {
                            pointNumStr = NumberUtils().makeCommaNumber(
                                Integer.parseInt(
                                    s.toString().replace(",", "")
                                )
                            )
                        } catch (numberFormat: NumberFormatException) {
                            Log.e(TAG,"numberFormat : $numberFormat")
                        }
                        view.setText(pointNumStr)
                        view.setSelection(pointNumStr.length)  //커서를 오른쪽 끝으로 보냄
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                //todo 팝업 기능 추가 시 hide는 여기서 처리
            }
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        gKeyboardVisibilityUtils.detachKeyboardListeners()
    }
}
