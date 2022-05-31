package com.meishe.modulecapture.viewmodel

import androidx.lifecycle.ViewModel
import com.meicam.sdk.NvsLiveWindow
import com.meicam.sdk.NvsStreamingContext


/**
 * * All rights reserved,Designed by www.meishesdk.com
 * @Author : lpf
 * @CreateDate : 2022/5/13 下午3:46
 * @Description : 负责业务逻辑
 * @Copyright :www.meishesdk.com Inc.All rights reserved.
 */
class CaptureLogicViewModel(private val mStreamingContext: NvsStreamingContext): ViewModel() {

    /**
     * 开启预览
     */
    fun startCapturePreview(deviceChanged: Boolean, deviceIndex: Int): Boolean {
        if (deviceChanged || mStreamingContext.streamingEngineState != NvsStreamingContext.STREAMING_ENGINE_STATE_CAPTUREPREVIEW) {
            if (!mStreamingContext.startCapturePreview(
                    deviceIndex,
                    NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_SUPER_HIGH,
                    NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_DONT_USE_SYSTEM_RECORDER or
                            NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_CAPTURE_BUDDY_HOST_VIDEO_FRAME or
                            NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_ENABLE_TAKE_PICTURE or
                            NvsStreamingContext.STREAMING_ENGINE_CAPTURE_FLAG_STRICT_PREVIEW_VIDEO_SIZE,
                    null
                )
            ) {
                return false
            }
        }
        return true
    }

    /**
     * 连接liveWindow
     */
    fun connectCapturePreviewWithLiveWindow(liveWindow: NvsLiveWindow) {
        mStreamingContext.connectCapturePreviewWithLiveWindow(liveWindow)
    }
}