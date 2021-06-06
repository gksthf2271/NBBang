package com.khs.nbbang.history.itemView

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.CviewHistoryBottomDialogBinding
import com.khs.nbbang.history.HistoryBottomRecyclerViewAdapter
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.utils.NumberUtils

class HistoryBottomItemView(val gPlace: Place) :
    BaseDialogFragment(DIALOG_TYPE.TYPE_HISTORY_BOTTOM_VIEW) {
    lateinit var mBinding: CviewHistoryBottomDialogBinding
    private val DEBUG = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cview_history_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        initView()
    }

    fun initView() {
        mBinding.let {
            it!!.btnClose.setOnClickListener {
                dismiss()
            }
            it!!.txtTitle.text = gPlace.placeName
            it!!.txtPrice.text = NumberUtils().makeCommaNumber(true, gPlace.price)
            it!!.recyclerNameList.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = HistoryBottomRecyclerViewAdapter(gPlace.joinPeopleList)
            }
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (this.isAdded) {
                    dismiss()
                    return true
                }
            }
        }
        return false
    }
}