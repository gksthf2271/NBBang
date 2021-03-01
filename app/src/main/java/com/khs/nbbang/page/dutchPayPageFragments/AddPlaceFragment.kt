package com.khs.nbbang.page.dutchPayPageFragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
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
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils
import kotlinx.android.synthetic.main.cview_edit_place.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


/**
 * TODO : 출시 전 해야될 것들
 *  1. 갤러리 연동하여, 프로필 설정 할 수 있는 기능 추가                                                    ---> 5순위
 *  2. 카카오 친구목록 UI 구현                                                                        ---> 4순위
 *  3. AddPeopleFragment AddMemberView 출력 시 하단 FavoriteMember 보이고, 선택하여 추가 할 수 있는 기능 구현 ---> 2순위
 *  4. UI 정리(테마 변경)                                                                            ---> 3순위
 *  5. AddPlaceFragment UI 일관성 유지되도록 수정 FloatingButton에 MotionView                           ---> 완료, 중복소스 존재
 *  6. AddPlaceRecyclerViewAdapter 에서 motionLayout 처리 간 이슈 발생                                 ---> 1순위
 *  -------------------------------------------- 목표일 !!!!!3월 28일 출시!!!!!!
 *  Group관리 기능은 2.0에 구현
 * **/


class AddPlaceFragment : BaseFragment() {
    lateinit var mBinding: FragmentAddPlaceBinding

    val mViewModel: PageViewModel by sharedViewModel()
//    lateinit var mRecyclerViewAdapter : AddPlaceRecyclerViewAdapter
    val TYPE_EDIT_PLACE_NAME: String = "TYPE_EDIT_PLACE_NAME"
    val TYPE_EDIT_PRICE: String = "TYPE_EDIT_PRICE"


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


    fun initView() {
        val rootView = mBinding.layoutGroup
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


//        mViewModel.saveSelectedPeople(mViewModel.mSelectedPeopleMap.value!!.size + 1, mutableListOf())
//        mRecyclerViewAdapter = AddPlaceRecyclerViewAdapter(requireActivity(), mViewModel, arrayListOf(),{
//            Log.v(TAG,"ItemClicked, index : $it")
//            //TODO item remove
//        },{ nbb ->
//            Log.v(TAG,"JoinBtnClicked, index : ${nbb.mPlaceIndex}")
//            mViewModel!!.selectPlace(nbb.mPlaceIndex)
//            showSelectPeopleDialog(nbb.mPlaceIndex.toString())
//        })
//
//        mBinding.recyclerView.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            addItemDecoration(HistoryItemDecoration(10))
//            adapter = mRecyclerViewAdapter
//        }

//        mBinding.btnAdd.setOnClickListener {
//            mViewModel.saveSelectedPeople(mViewModel.mSelectedPeopleMap.value!!.size + 1, mutableListOf())
//        }
        rootView.addView(createPlaceInfoView(rootView, inflater), rootView.childCount)
        mBinding.btnAdd.setOnClickListener {
            rootView.addView(createPlaceInfoView(rootView, inflater), rootView.childCount)
        }
    }

    private fun addObserver() {
//        mViewModel.mSelectedPeopleMap.observe(requireActivity(), Observer {selectedPeopleMap ->
//            var list = arrayListOf<NBB>()
//            list.addAll(selectedPeopleMap.values.toList())
//            mRecyclerViewAdapter.setItemList(list)
//        })

        mViewModel.mNBBLiveData.observe(requireActivity(), Observer {
            Log.v(TAG, "mNBBLiveData, Observer(...) : $it")
            mViewModel.clearSelectedPeople()
        })
    }

    private fun showSelectPeopleDialog(tag: String) {
        Log.v(TAG, "showSelectPeopleDialog(...)")
        var selectPeopleDialog = SelectPeopleDialogFragment.getInstance()
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

        var placeIndex = rootView.childCount + 1

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

        mBinding.viewModel.let {
            it!!.updateJoinPlaceCount(placeIndex)

            it!!.mSelectedPeopleMap.observe(requireActivity(), Observer {
                it.get(infoView.tag as Int) ?: return@Observer
                Log.v(TAG, "_selectedPeopleMap, Observer(...) : $it")
                if (it!!.get(infoView.tag as Int)!!.mMemberList.isEmpty()) {
                    hideAddedPeopleView(infoView)
                } else {
                    showAddedPeopleView(infoView, it!!.get(infoView.tag as Int)!!)
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


}
