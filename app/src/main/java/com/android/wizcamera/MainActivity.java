/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.wizcamera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.wizcamera.utils.DimensUtil;
import com.android.wizcamera.utils.FileUtil;
import com.android.wizcamera.view.CapturePictureButton;
import com.wizcamera.AspectRatio;
import com.wizcamera.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This demo app saves the taken picture to a constant file.
 * $ adb pull /sdcard/Android/data/com.google.android.cameraview.demo/files/Pictures/picture.jpg
 */
public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, AspectRatioFragment.Listener, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private static final String FRAGMENT_PERMISSION = "permission";
    private static final String FRAGMENT_PICTURE = "picture";

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    private int mCurrentFlash;

    private CameraView mCameraView;

    private Handler mBackgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mCameraView = (CameraView) findViewById(R.id.camera);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        CapturePictureButton fab = (CapturePictureButton) findViewById(R.id.take_picture);
        fab.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_PHONE_STATE); //bugly
        try {
            if (checkPermission(permissions)) {
                mCameraView.start();
            }
        } catch (Exception e) {
            Log.e(TAG, "start camera fail", e);
        }
    }

    private boolean checkPermission(List<String> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return true;
        }
        int count = 0;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                count++;
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ConfirmationDialogFragment.newInstance(R.string.camera_permission_confirmation,
                        new String[]{permission}, REQUEST_CAMERA_PERMISSION, R.string.camera_permission_not_granted)
                        .show(getSupportFragmentManager(), FRAGMENT_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
            }
        }
        Log.e(TAG, "checkPermission:" + count);
        return count == permissions.size();
    }

    @Override
    protected void onPause() {
        try {
            mCameraView.stop();
        } catch (Exception e) {
            Log.e(TAG, "stop camera fail", e);
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_permission_not_granted, Toast.LENGTH_SHORT).show();
                }
                // No need to start camera here; it is handled by onResume
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.custom_aspect_ratio:
                Toast.makeText(this, "custom aspect ratio", Toast.LENGTH_SHORT).show();
                int displayW = DimensUtil.getDisplayWidth(this);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCameraView.getLayoutParams();
                params.height = displayW * 4 / 3;
                mCameraView.setLayoutParams(params);
                break;
            case R.id.aspect_ratio:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (mCameraView != null && fragmentManager.findFragmentByTag(FRAGMENT_PERMISSION) == null) {
                    final Set<AspectRatio> ratios = mCameraView.getSupportedAspectRatios();
                    final AspectRatio currentRatio = mCameraView.getAspectRatio();
                    AspectRatioFragment.newInstance(ratios, currentRatio).show(fragmentManager,
                            FRAGMENT_PERMISSION);
                }
                return true;
            case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ? CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
        if (mCameraView != null) {
            Toast.makeText(this, ratio.toString(), Toast.LENGTH_SHORT).show();
            mCameraView.setAspectRatio(ratio);
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback = new CameraView.Callback() {

        @Override
        public void onCameraError(CameraView cameraView) {
            Log.e(TAG, "onCameraError");
        }

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            producePicture2(data);
        }
    };

    public void producePicture2(final byte[] jpeg) {
        getBackgroundHandler().post(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                File file = null;
                try {
                    File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "wizcamera");
                    FileUtil.createDir(directory);
                    file = FileUtil.generateName(directory);
                    os = new FileOutputStream(file);
                    os.write(jpeg);
                    os.close();

                    MainActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                    // PictureDialogFragment.newInstance(file.getAbsolutePath()).show(getSupportFragmentManager(), FRAGMENT_PICTURE);
                    Log.e(TAG, "file size:" + file.length() + ",getAbsolutePath:" + file.getAbsolutePath() + ",Thread:" + Thread.currentThread().getName());
                } catch (Exception e) {
                    Log.w(TAG, "--> Cannot write to " + (file == null ? "" : file.getAbsolutePath()) + e);
                } catch (Error error) {
                    Log.w(TAG, "--> Cannot write to " + (file == null ? "" : file.getAbsolutePath()) + error);
                    System.gc();
                } finally {
                    FileUtil.closeQuietly(os);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_picture:
                if (mCameraView != null) {
                    mCameraView.takePicture();
                }
                break;
            default:
                break;
        }
    }
}
