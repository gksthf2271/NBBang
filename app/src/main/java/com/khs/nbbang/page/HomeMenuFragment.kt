package com.khs.nbbang.page

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
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
        initObserver()
    }

    fun initObserver() {
        mBinding.viewModel.let {
            it!!.mLoginCookie.observe(requireActivity(), Observer {
                Log.d(TAG, "Login Cookie >>> ${it.cookieData}")
                if (!TextUtils.isEmpty(it.cookieData)) {
                    loadFreeUserFragment()
                }
            })

            it!!.mIsLogin.observe(requireActivity(), Observer {
                Log.v(TAG,"TEST, isLogin : $it")
                when (it) {
                    true -> loadFreeUserFragment()
                    false -> loadFreeUserFragment()
                }
            })
        }
    }

    fun loadKakaoUserFragment() {
//        val bundle = bundleOf("api" to myDataset[position])
//
//        Navigation.findNavController(holder.item).navigate(
//            R.id.action_leaderboard_to_userProfile,
//            bundle)
    }

    fun loadFreeUserFragment() {
        Navigation.findNavController(requireView()).navigate(
            R.id.action_home_menu_to_dutch_pay_home)
    }
}