package com.meishe.libcommon

import android.content.Context
import android.os.Build


/**
 * * All rights reserved,Designed by www.meishesdk.com
 * @Author : lpf
 * @CreateDate : 2022/5/13 下午1:17
 * @Description :
 * @Copyright :www.meishesdk.com Inc.All rights reserved.
 */
object AndroidOS {

    @JvmField
    var USE_SCOPED_STORAGE: Boolean = false

    fun initConfig(context: Context?) {
        //android11的适配版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            USE_SCOPED_STORAGE = true
        }
    }

}