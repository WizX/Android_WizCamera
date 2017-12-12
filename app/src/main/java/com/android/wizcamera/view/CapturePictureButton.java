package com.android.wizcamera.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2017/12/12 20:01<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class CapturePictureButton extends FloatingActionButton {
    private OvershootInterpolator interpolator;

    public CapturePictureButton(Context context) {
        this(context, null);
    }

    public CapturePictureButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CapturePictureButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        interpolator = new OvershootInterpolator();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchAnimation(0.88f);
                break;
            case MotionEvent.ACTION_UP:
                touchAnimation(1f);
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void touchAnimation(float scale) {
        animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(300)
                .setInterpolator(interpolator)
                .start();
    }
}
