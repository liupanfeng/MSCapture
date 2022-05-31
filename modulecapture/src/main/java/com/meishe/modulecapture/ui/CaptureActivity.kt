package com.meishe.modulecapture.ui

import androidx.lifecycle.ViewModelProvider
import com.meishe.libbase.BaseActivity
import com.meishe.modulecapture.databinding.ActivityCaptureBinding
import com.meishe.modulecapture.factory.AppModelFactory
import com.meishe.modulecapture.viewmodel.CaptureLogicViewModel
import com.meishe.modulecapture.viewmodel.CaptureViewModel

class CaptureActivity : BaseActivity() {

    override val mViewBinding by bindLayout<ActivityCaptureBinding>()
//    private lateinit var mViewModel: CaptureViewModel
//    private lateinit var mLogicViewModel: CaptureLogicViewModel


    override fun bindViewModel() {
//        mViewModel = ViewModelProvider(this,
//            AppModelFactory(mStreamingContext))[CaptureViewModel::class.java]
//        mLogicViewModel = ViewModelProvider(this,
//            AppModelFactory(mStreamingContext))[CaptureLogicViewModel::class.java]
    }

    override fun initData() {
//        mLogicViewModel.connectCapturePreviewWithLiveWindow(mViewBinding.liveWindow)
//        mLogicViewModel.startCapturePreview(true,1)
    }



}