package com.khs.nbbang.page.dutchPayPageFragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentResultPageBinding
import com.khs.nbbang.history.itemView.PlaceBottomItemView
import com.khs.nbbang.page.adapter.ResultRecyclerViewAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.utils.NumberUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ResultPageFragment : BaseFragment() {
    lateinit var mBinding: FragmentResultPageBinding
    val mViewModel: PageViewModel by sharedViewModel()
    val KEY_TITLE = "KEY_TITLE"
    val KEY_DESCRIPTION = "KEY_DESCRIPTION"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mViewModel
        initView()
    }

    fun initView() {
        mBinding.txtNotifyCopy.setOnClickListener {
            Log.v(TAG, "Notify Btn Clicked!")
            mBinding.viewModel?.let {
                showHistoryCheckerDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.viewModel?.let { pageViewModel ->
            CoroutineScope(Dispatchers.Default).launch {
                pageViewModel.clearDutchPayMap()
                CoroutineScope(Dispatchers.Main).launch {
                    updateList()
                }
                pageViewModel.resultNBB()
                if (pageViewModel.gIsSavedResult) pageViewModel.gIsSavedResult = false
            }
        }
    }

    private fun updateList() {
        mBinding.viewModel?.let { pageViewModel ->
            pageViewModel.gNBBResultItem.observe(requireActivity(), Observer {
                Log.v(TAG, "khs, observer(...)")
                mBinding.recyclerViewResult.apply {
                    setHasFixedSize(true)
                    addItemDecoration(HistoryItemDecoration(2))
                    layoutManager = LinearLayoutManager(context)
                    adapter = ResultRecyclerViewAdapter(it.place) {
                        Log.v(TAG, "onClicked(...), item : $it")
                        val dialog = PlaceBottomItemView(it)
                        dialog.show(requireActivity().supportFragmentManager, null)
                    }

                    var totalPrice = 0
                    for (item in it.place) {
                        Log.v(TAG, "khs, price : ${item.price}")
                        totalPrice += item.price
                    }

                    Log.v(TAG, "khs, totalPrice : $totalPrice")
                    mBinding.txtPrice.text = NumberUtils().makeCommaNumber(true, totalPrice)
                }
            })
        }
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG, "makeCustomLoadingView(...)")
        return null
    }

    private fun showHistoryCheckerDialog() {
        Log.v(TAG, "showSelectPeopleDialog(...)")
        HistoryCheckerDialogFragment.getInstance().apply {
            this.arguments = Bundle().apply {
                this.putCharSequence(KEY_TITLE, this@ResultPageFragment.mBinding.txtTitle.text)
                this.putCharSequence(
                    KEY_DESCRIPTION,
                    this@ResultPageFragment.mBinding.viewModel!!.resultNBB()
                )
            }
        }.show(requireActivity().supportFragmentManager, tag)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                //todo 팝업 기능 추가 시 hide는 여기서 처리
            }
        }
        return false
    }
}