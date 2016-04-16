package com.example.wlbreath.zhihudaily.view;

import android.view.View;
import android.view.ViewGroup;

import com.example.wlbreath.zhihudaily.bean.ThemeBean;
import com.example.wlbreath.zhihudaily.bean.ThemeStory;

import java.util.List;

/**
 * Created by wlbreath on 16/4/10.
 */
public interface IThemePageView {
    void displayContentArea(boolean show);

    void displayErrorPage(boolean show);

    void showErrorMessage(View view, String msg);

    void update(ThemeBean theme);

    void appendStories(List<ThemeStory> stories);

    void startRefreshAnimation();

    void stopRefreshAnimation();

    ViewGroup getContentArea();

    ViewGroup getErrorPage();
}
