package com.khs.nbbang.freeUser.pageFragment

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentResultPageBinding
import com.khs.nbbang.freeUser.viewModel.PageViewModel

class ResultPageFragment : BaseFragment() {
    lateinit var mBinding : FragmentResultPageBinding

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
        mBinding.txtNotifyCopy.setOnClickListener {
            val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
            val clip: ClipData = ClipData.newPlainText(mBinding.txtTitle.text, mBinding.txtResult.text)
            clipboard.let {
                it!!.setPrimaryClip(clip)
                Toast.makeText(requireContext(),"클립보드에 영수증이 복사되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.viewModel.let {
            it!!.clearDutchPayMap()
            mBinding.txtResult.text = it!!.resultNBB()
        }
    }
}