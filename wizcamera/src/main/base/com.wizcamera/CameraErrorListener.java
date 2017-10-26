package com.wizcamera;

/**
 * description： 错误日志上报  <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2017/10/20 16:42<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public interface CameraErrorListener {
    /**
     * @param e 异常 上报
     */
    void onCameraError(Exception e);

    /**
     * @param msg 错误信息上报
     */
    void onCameraEvent(String... msg);
}
