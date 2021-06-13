//package com.khs.nbbang.shareMessage
//
//import android.os.Bundle
//import androidx.databinding.DataBindingUtil
//import com.khs.nbbang.R
//import com.khs.nbbang.base.BaseActivity
//import com.khs.nbbang.databinding.ActivityShareBinding
//
//class ShareActivity : BaseActivity() {
//    lateinit var mBinding: ActivityShareBinding
//    val EXTRA_NBB_RESULT_NAME = "NBB_RESULT"
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_share)
//        initView()
//    }
//
//    fun initView() {
//        intent.let {
//            it!!.getSerializableExtra(EXTRA_NBB_RESULT_NAME)
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//    }
//
//    override fun onPause() {
//        super.onPause()
//    }
//
//    override fun onResume() {
//        super.onResume()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }
//}