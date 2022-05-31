package com.meishe.mscapture

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.LogUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.meicam.sdk.NvsStreamingContext
import com.meishe.libbase.BaseActivity
import com.meishe.libcommon.AndroidOS
import com.meishe.libcommon.file.FileUtils
import com.meishe.libcommon.toast
import com.meishe.modulecapture.ui.CaptureActivity
import com.meishe.mscapture.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

import org.koin.core.parameter.parametersOf

class MainActivity: BaseActivity() {

    override val mViewBinding by bindLayout<ActivityMainBinding>()

    override fun initData() {
        requestPermission()
    }

    override fun initListener(){
        mViewBinding.btnStartCapture.click {
            start(CaptureActivity::class.java)
        }
    }

    /**
     * 初始化模型
     */
    @SuppressLint("CheckResult")
    private fun initModels() {
        Observable.just(1).observeOn(Schedulers.io()).subscribe(Consumer {
            doInitModel()
        })
    }

    private fun doInitModel() {
        var modelPath: String? = null
        var licensePath: String? = null
        var faceModelName: String? = null
        var className: String? = null

        modelPath = "/facemode/ms/ms_face_v1.2.2.model"
        faceModelName = "ms_face_v1.2.2.model"
        className = "facemode/ms"
        licensePath = ""


        /**
         * 模型文件需要是本地文件路径，所以从assert内置拷贝到本地
         * The model file needs to be a local file path, so copy it from assert built-in to local
         */
        val copySuccess: Boolean =
            FileUtils.copyFileIfNeed(this@MainActivity, faceModelName, className)
        LogUtils.d(TAG, "copySuccess-->$copySuccess")
        var rootDir = applicationContext.getExternalFilesDir("")
        if (AndroidOS.USE_SCOPED_STORAGE) {
            rootDir = applicationContext.filesDir
        }
        val destModelDir = rootDir.toString() + modelPath
        //1.2.2人脸模型初始化
        val initSuccess = NvsStreamingContext.initHumanDetection(
            App.getContext(),
            destModelDir, licensePath,
            NvsStreamingContext.HUMAN_DETECTION_FEATURE_FACE_LANDMARK or NvsStreamingContext.HUMAN_DETECTION_FEATURE_FACE_ACTION
        )

        "initSuccess-->$initSuccess".logD()

        //240点位模型的初始化
        val pePath = "assets:/facemode/ms/240/pe240_ms_v1.0.0.dat"
        val peSuccess = NvsStreamingContext.setupHumanDetectionData(
            NvsStreamingContext.HUMAN_DETECTION_DATA_TYPE_PE240,
            pePath
        )

        "ms240 peSuccess-->$peSuccess".logD()


        /**
         * 假脸模型初始化
         * The fake Face model is initialized
         */
        val fakeFacePath = "assets:/facemode/common/fakeface.dat"
        val fakefaceSuccess =
            NvsStreamingContext.setupHumanDetectionData(
                NvsStreamingContext.HUMAN_DETECTION_DATA_TYPE_FAKE_FACE,
                fakeFacePath
            )

        "fakefaceSuccess-->$fakefaceSuccess".logD()


        val segPath = "assets:/facemode/ms/ms_humanseg_v1.0.7.model"
        val segSuccess = NvsStreamingContext.initHumanDetectionExt(
            App.getContext(),
            segPath, null, NvsStreamingContext.HUMAN_DETECTION_FEATURE_SEGMENTATION_BACKGROUND
        )

        "ms segSuccess-->$segSuccess".logD()


        val segSkyPath = "assets:/facemode/ms/ms_skyseg_v1.0.0.model"
        val segSkySuccess = NvsStreamingContext.initHumanDetectionExt(
            App.getContext(),
            segSkyPath, null, NvsStreamingContext.HUMAN_DETECTION_FEATURE_SEGMENTATION_SKY
        )

        "ms segSkySuccess-->$segSkySuccess".logD()


        val handPath = "assets:/facemode/ms/ms_hand_v1.0.0.model"
        val handSuccess = NvsStreamingContext.initHumanDetectionExt(
            App.getContext(),
            handPath,
            null,
            NvsStreamingContext.HUMAN_DETECTION_FEATURE_HAND_LANDMARK or NvsStreamingContext.HUMAN_DETECTION_FEATURE_HAND_ACTION
        )

        "ms handSuccess-->$handSuccess".logD()


        /**
         * avatar表情
         * Avatar look
         */
        modelPath = rootDir.toString() + "/facemode/common/ms_expression_v1.0.2.model"
        faceModelName = "ms_expression_v1.0.2.model"
        val expressionModel = "facemode/common"
        FileUtils.copyFileIfNeed(this@MainActivity, faceModelName, expressionModel)
        NvsStreamingContext.initHumanDetectionExt(
            App.getContext(),
            modelPath,
            null,
            NvsStreamingContext.HUMAN_DETECTION_FEATURE_AVATAR_EXPRESSION
        )
    }

    /**
     * 获取授权
     */
    private fun requestPermission() {
        XXPermissions.with(this).permission(Permission.READ_EXTERNAL_STORAGE)
            .permission(Permission.CAMERA).permission(Permission.WRITE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        initModels()
                    } else {
                        getString(R.string.get_pert_permission).toast()
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never) {
                        getString(R.string.deny_permisson).toast()
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(mContext, permissions);
                    } else {
                        getString(R.string.get_permission_error).toast()
                    }
                }
            })
    }
}