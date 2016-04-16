package com.example.wlbreath.zhihudaily.view;

import android.graphics.Bitmap;

/**
 * Created by wlbreath on 16/4/7.
 */
public interface IWelcomePageView {
    void updateTitle(String title);

    void updateBackground(Bitmap bg);

    void toMainActivity(long delay);
}
