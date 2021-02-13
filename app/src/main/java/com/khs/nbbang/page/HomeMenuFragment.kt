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
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class HomeMenuFragment : BaseFragment() {
    lateinit var mBinding : com.khs.nbbang.databinding.FragmentHomeMenuBinding
    val mViewModel: LoginViewModel by sharedViewModel()

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
        mBinding.viewModel = mViewModel

        initView()
    }

    private fun initView(){
        mBinding.btnFree.setOnClickListener {
            loadFreeUserFragment()
        }

        mBinding.btnKakaoLogin.setOnClickListener {
            mBinding.viewModel.let {
                it!!.mLoginCookie.observe(requireActivity(), Observer {
                    Log.d(TAG, "Login Cookie >>> ${it.cookieData}")
                    if (!TextUtils.isEmpty(it.cookieData)) {
                        loadKakaoUserFragment()
                    }
                })
            }
        }
    }
    fun loadKakaoUserFragment() {
        Toast.makeText(requireContext(),"카카오 API 실행", Toast.LENGTH_SHORT).show()

//        val bundle = bundleOf("api" to myDataset[position])
//
//        Navigation.findNavController(holder.item).navigate(
//            R.id.action_leaderboard_to_userProfile,
//            bundle)
    }

    fun loadFreeUserFragment() {
        requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_home_menu_to_dutch_pay_home)
    }
}