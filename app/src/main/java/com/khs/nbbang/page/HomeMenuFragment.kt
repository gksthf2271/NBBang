package com.khs.nbbang.page

import android.os.Bundle
import android.text.TextUtils
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
    }

    override fun onResume() {
        super.onResume()
        mPageViewModel.let {
            it!!.clearPageViewModel()
        }
    }

    private fun initView(){
        mBinding.btnFree.setOnClickListener {
            loadFreeUserFragment()
        }

        mBinding.btnKakaoLogin.setOnClickListener {
            mBinding.viewModel.let {
                it!!.mLoginCookie.observe(requireActivity(), Observer {
                    Log.d(TAG, "Login Cookie >>> ${it.accessToken}")
                    if (!TextUtils.isEmpty(it.accessToken)) {
                        loadKakaoUserFragment()
                    }
                })
            }
        }

        mBinding.viewModel.let {
            it!!.mMyDataFrom.observe(requireActivity(), Observer {
                Log.v(
                    TAG, "MyData id : ${it.id}"
                            + "\n name : ${it.properties?.get("nickname")}"
                            + "\n profile_image : ${it.properties?.get("profile_image")}"
                            + "\n thumbnail_image : ${it.properties?.get("thumbnail_image")}"
                )
            })
        }
    }

    fun loadKakaoUserFragment() {
        Toast.makeText(requireContext(),"카카오 API 실행", Toast.LENGTH_SHORT).show()
    }

    fun loadFreeUserFragment() {
        requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_home_menu_to_dutch_pay_home)
    }
}