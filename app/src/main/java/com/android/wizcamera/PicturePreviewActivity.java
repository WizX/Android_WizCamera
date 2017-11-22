package com.android.wizcamera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2017/11/22 16:15<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class PicturePreviewActivity extends FragmentActivity {
    public static final String PICTURE_PATH = "picturePath";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (savedInstanceState == null) {
            String picturePath = getIntent() != null && getIntent().hasExtra(PICTURE_PATH) ? getIntent().getStringExtra(PICTURE_PATH) : "";
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                    PicturePreviewFragment.newInstance(picturePath), "picture").commitAllowingStateLoss();
        }
    }
}
