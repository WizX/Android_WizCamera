package com.wizcamera;

import android.support.annotation.Nullable;

abstract class CameraImpl {

    protected final CameraListener mCameraListener;
    protected final PreviewImpl mPreview;

    CameraImpl(CameraListener callback, PreviewImpl preview) {
        mCameraListener = callback;
        mPreview = preview;
    }

    /**
     * camera开始
     */
    abstract void start();
    /**
     * camera停止
     */
    abstract void stop();


    /**
     * 切换前后摄像头
     * @param facing 前 ，后
     */
    abstract void setFacing( int facing);

    /**
     * 设置闪光灯
     * @param flash 自动，开，关，常亮
     */
    abstract void setFlash( int flash);

    abstract void setVideoBitRate(int videoBitRate);

    abstract void captureImage();

    abstract boolean isCameraOpened();
    abstract boolean frontCameraOnly();

    abstract void setCameraErrorListener(CameraErrorListener listener);

}
