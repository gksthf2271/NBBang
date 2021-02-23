package com.khs.nbbang.page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.utils.FragmentUtils
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeMenuFragment : BaseFragment() {
    lateinit var mBinding : com.khs.nbbang.databinding.FragmentHomeMenuBinding
    val mLoginViewModel: LoginViewModel by sharedViewModel()
    val mPageViewModel : PageViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mLoginViewModel

        initView()
        addObserver()
    }

    override fun onResume() {
        super.onResume()
        mPageViewModel.let {
            it!!.clearPageViewModel()
        }
    }

    private fun initView(){
        mBinding.btnFree.setOnClickListener {
            loadDutchPayFragment()
        }
    }

    fun addObserver() {
        mBinding.viewModel.let {
            it!!.mMyData.observe(requireActivity(), Observer {
                it ?: return@Observer
                Log.v(
                    TAG, "MyData id : ${it.id}"
                            + "\n name : ${it.properties?.get("nickname")}"
                            + "\n profile_image : ${it.properties?.get("profile_image")}"
                            + "\n thumbnail_image : ${it.properties?.get("thumbnail_image")}"
                )
            })

            it!!.mIsLogin.observe(requireActivity(), Observer {
                Log.d(TAG, "isLogin >>> $it")
                if (it && FragmentUtils().currentFragmentClassName(requireActivity().nav_host_fragment).equals(TAG)) {
                    loadDutchPayFragment()
                }
            })
        }
    }

    fun loadDutchPayFragment() {
        requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_go_to_dutch_pay)
    }
}