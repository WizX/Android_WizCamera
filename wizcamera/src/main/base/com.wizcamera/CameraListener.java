package com.wizcamera;


/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2017/10/20 16:46<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public interface CameraListener {

    void onCameraOpened();

    void onCameraClosed();

    void onPictureTaken(byte[] jpeg);
}
