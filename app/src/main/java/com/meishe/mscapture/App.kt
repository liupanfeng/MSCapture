package com.meishe.mscapture

import android.content.Context
import com.meishe.libbase.BaseApplication
import com.meishe.libcommon.AndroidOS
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * * All rights reserved,Designed by www.meishesdk.com
 * @Author : lpf
 * @CreateDate : 2022/5/12 下午5:45
 * @Description :
 * @Copyright :www.meishesdk.com Inc.All rights reserved.
 */

class App :BaseApplication() {

    companion object{
        fun getContext(): Context {
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidOS.initConfig(mContext)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            androidFileProperties()
            fragmentFactory()
            modules(allModules)
        }

    }

    override fun getApplicationId(): String {
        return BuildConfig.APPLICATION_ID
    }

    override fun getApplicationVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    override fun getApplicationCode(): String {
        return BuildConfig.VERSION_CODE.toString()
    }
}