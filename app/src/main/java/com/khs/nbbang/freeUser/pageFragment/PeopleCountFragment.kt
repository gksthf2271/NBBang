package com.khs.nbbang.freeUser.pageFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentPeopleCountBinding
import com.khs.nbbang.freeUser.viewModel.PageViewModel


class PeopleCountFragment : BaseFragment() {
    lateinit var mBinding: FragmentPeopleCountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_people_count, container, false)
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
        initView()
    }

    fun initView() {
        mBinding.viewModel.let {
            it!!._NNBLiveData.observe(requireActivity(), Observer {
                mBinding.txtCount.text = it.mPeopleCount.toString()
            })
        }
    }
}