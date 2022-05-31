package com.meishe.libbase

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.meicam.sdk.NvsStreamingContext


/**
 * * All rights reserved,Designed by www.meishesdk.com
 * @Author : lpf
 * @CreateDate : 2022/5/12 下午5:41
 * @Description :
 * @Copyright :www.meishesdk.com Inc.All rights reserved.
 */
open abstract class BaseApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var INSTANCE: BaseApplication

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        mContext = this
        val licensePath = "assets:/meishesdk.lic"
        initStreamingContext(mContext,licensePath)
    }


    fun initStreamingContext(context: Context, licPath: String): NvsStreamingContext {
        return NvsStreamingContext.init(context, licPath,
            NvsStreamingContext.STREAMING_CONTEXT_FLAG_SUPPORT_4K_EDIT
                    or NvsStreamingContext.STREAMING_CONTEXT_FLAG_ENABLE_HDR_DISPLAY_WHEN_SUPPORTED
                    or NvsStreamingContext.STREAMING_CONTEXT_FLAG_INTERRUPT_STOP_FOR_INTERNAL_STOP)
    }


    abstract fun getApplicationId(): String

    abstract fun getApplicationVersion(): String

    abstract fun getApplicationCode(): String
}