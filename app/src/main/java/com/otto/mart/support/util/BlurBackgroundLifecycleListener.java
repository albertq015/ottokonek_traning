package com.otto.mart.support.util;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.RenderScript;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otto.mart.R;

public class BlurBackgroundLifecycleListener implements LifecycleObserver {

    private static String TAG = BlurBackgroundLifecycleListener.class.getSimpleName();

    private View mBlurView;

    private Bitmap mBitmap;

    private BitmapDrawable mBlurBackground;

    private FragmentActivity mFragmentActivity;

    private RSBlurProcessor mBlurProcessor;

    private static boolean sIsButtonPressed;

    public static void setButtonPressed(boolean backButtonPressed) {
        sIsButtonPressed = backButtonPressed;
    }

    public static boolean isButtonPressed() {
        return sIsButtonPressed;
    }

    public BlurBackgroundLifecycleListener(Context context) {
        mBlurProcessor = new RSBlurProcessor(RenderScript.create(context));
        mFragmentActivity = (FragmentActivity) context;
        initBlurBackground();
    }

    public void initBlurBackground() {
        View rootView = mFragmentActivity.findViewById(android.R.id.content);
        rootView.setDrawingCacheEnabled(true);
        rootView.post( () -> {
            mBitmap = mBlurProcessor.getBitmapFromView(rootView);
            mBitmap = mBlurProcessor.blur(mBitmap, 15, 5);
            mBlurBackground = new BitmapDrawable(mFragmentActivity.getResources(), mBitmap);
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void fragmentOnResumeState() {
        if (mBlurView == null) return;
        if (mBlurView.getParent() == null) return;
        ((ViewGroup) mBlurView.getParent()).removeView(mBlurView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void fragmentOnPauseState() {
        if (isButtonPressed()) return;
        LayoutInflater inflater = mFragmentActivity.getLayoutInflater();
        mBlurView = inflater.inflate(R.layout.activity_blur, null);
        mBlurView.setBackground(mBlurBackground);
        mFragmentActivity.getWindow().addContentView(mBlurView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void fragmentOnStopState() {
        setButtonPressed(false);
    }

}