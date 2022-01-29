package com.khs.nbbang.page

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentHomeMenuBinding
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.page.viewModel.SelectMemberViewModel
import com.khs.nbbang.utils.FragmentUtils
import com.khs.nbbang.utils.LogUtil
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class HomeMenuFragment : BaseFragment() {
    lateinit var mBinding : FragmentHomeMenuBinding
    private val mLoginViewModel: LoginViewModel by sharedViewModel()
    private val mPageViewModel : PageViewModel by sharedViewModel()
    private val mSelectMemberViewModel : SelectMemberViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeMenuBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mLoginViewModel
        mBinding.fragment = this
        initView()
        addObserver()
    }

    override fun onResume() {
        super.onResume()
        mPageViewModel.clearPageViewModel()
        mSelectMemberViewModel.clearSelectedMemberHashMap()
    }

    override fun makeCustomLoadingView(): Dialog? {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "makeCustomLoadingView(...)")
        return null
    }

    private fun initView(){
        mBinding.btnFree.setOnClickListener {
            loadDutchPayFragment()
        }
    }

    private fun addObserver() {
        mBinding.viewModel?.let { it ->
            it.gMyData.observe(requireActivity(), Observer {
                it ?: return@Observer
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "MyData id : ${it.id}"
                            + "\n name : ${it.properties?.get("nickname")}"
                            + "\n profile_image : ${it.properties?.get("profile_image")}"
                            + "\n thumbnail_image : ${it.properties?.get("thumbnail_image")}"
                )
            })

            it.gIsLogin.observe(requireActivity(), Observer {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "isLogin >>> $it")
                if (it && FragmentUtils().currentFragmentClassName(requireActivity().nav_host_fragment).equals(TAG_CLASS)) {
                    loadDutchPayFragment()
                }
            })
        }
    }

    private fun loadDutchPayFragment() {
        requireActivity().nav_host_fragment.findNavController().navigate(R.id.action_go_to_dutch_pay)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                //todo 팝업 기능 추가 시 hide는 여기서 처리
            }
        }
        return false
    }
}