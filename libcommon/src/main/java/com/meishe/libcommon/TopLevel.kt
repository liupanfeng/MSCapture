package com.meishe.libcommon

import android.widget.Toast
import com.meishe.libbase.BaseApplication


/**
 * * All rights reserved,Designed by www.meishesdk.com
 * @Author : lpf
 * @CreateDate : 2022/5/12 下午6:26
 * @Description :
 * @Copyright :www.meishesdk.com Inc.All rights reserved.
 */

fun <T> T.toast() = Toast.makeText(BaseApplication.INSTANCE, this.toString(), Toast.LENGTH_LONG).show()