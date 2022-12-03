package com.khs.nbbang.page.dutchPayPageFragments

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentResultPageBinding
import com.khs.nbbang.history.itemView.PlaceBottomItemView
import com.khs.nbbang.page.adapter.ResultRecyclerViewAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.utils.LogUtil
import com.khs.nbbang.utils.NumberUtils
import kotlinx.coroutines.*
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
    ): View {
        mBinding = FragmentResultPageBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mViewModel
        initView()
        addObserver()
    }

    fun initView() {
        mBinding.txtNotifyCopy.setOnClickListener {
            LogUtil.vLog(LOG_TAG, TAG_CLASS, "Notify Btn Clicked!")
            mBinding.viewModel?.let {
                showHistoryCheckerDialog()
            }
        }
        mBinding.recyclerViewResult.apply {
            setHasFixedSize(true)
            addItemDecoration(HistoryItemDecoration(2))
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        requestDutchPay()
    }

    private fun requestDutchPay() {
        mBinding.viewModel?.let { pageViewModel ->
            CoroutineScope(Dispatchers.IO).launch {
                pageViewModel.resultNBB()
                if (pageViewModel.gIsSavedResult) pageViewModel.gIsSavedResult = false
            }
        }
    }

    private fun addObserver() {
        mBinding.viewModel?.let { pageViewModel ->
            pageViewModel.gNBBResultItem.observe(requireActivity(), Observer { nbbResult ->
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "gNBBResultItem > observer(...)")
                mBinding.recyclerViewResult.apply {
                    adapter = ResultRecyclerViewAdapter(nbbResult.place) {
                        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onClicked > item : $it")
                        val dialog = PlaceBottomItemView(it)
                        dialog.show(requireActivity().supportFragmentManager, null)
                    }

                    var totalPrice = 0
                    for (item in nbbResult.place) {
                        LogUtil.vLog(LOG_TAG, TAG_CLASS, "gNBBResultItem > price : ${item.price}")
                        totalPrice += item.price
                    }

                    LogUtil.vLog(LOG_TAG, TAG_CLASS, "gNBBResultItem > totalPrice : $totalPrice")
                    mBinding.txtPrice.text = NumberUtils().makeCommaNumber(true, totalPrice)
                }
            })
        }
    }

    override fun makeCustomLoadingView(): Dialog? {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "makeCustomLoadingView(...)")
        return null
    }

    private fun showHistoryCheckerDialog() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "showSelectPeopleDialog(...)")
        mBinding.viewModel?.let { pageViewModel ->
            HistoryCheckerDialogFragment.getInstance().apply {
                this.arguments = Bundle().apply {
                    this.putCharSequence(KEY_TITLE, this@ResultPageFragment.mBinding.txtTitle.text)
                    this.putCharSequence(
                        KEY_DESCRIPTION,
                        pageViewModel.resultNBB()
                    )
                }
            }.show(requireActivity().supportFragmentManager, tag)
        }
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