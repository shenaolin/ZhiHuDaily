package com.example.wlbreath.zhihudaily;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by wlbreath on 16/3/24.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(getApplicationContext());
    }
}
