package com.khs.nbbang.page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

//TODO : Login화면 분리 필요, 화면 별 순서 확립해야됨

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
                Log.v(
                    TAG, "MyData id : ${it.id}"
                            + "\n name : ${it.properties?.get("nickname")}"
                            + "\n profile_image : ${it.properties?.get("profile_image")}"
                            + "\n thumbnail_image : ${it.properties?.get("thumbnail_image")}"
                )
            })

            it!!.mIsLogin.observe(requireActivity(), Observer {
                Log.d(TAG, "isLogin >>> $it")
                if (it) {
                    loadDutchPayFragment()
                }
            })
        }
    }

    fun loadDutchPayFragment() {
        requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_home_menu_to_dutch_pay_home)
    }
}