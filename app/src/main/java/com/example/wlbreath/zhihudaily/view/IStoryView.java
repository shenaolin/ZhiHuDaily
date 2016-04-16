package com.example.wlbreath.zhihudaily.view;

import android.view.View;
import android.view.ViewGroup;

import com.example.wlbreath.zhihudaily.bean.StoryBean;
import com.example.wlbreath.zhihudaily.bean.StoryExtraInfoBean;

/**
 * Created by wlbreath on 16/4/11.
 */
public interface IStoryView {
    void displayContentArea(boolean show);

    void displayErrorPage(boolean show);

    ViewGroup getContentArea();

    ViewGroup getErrorPage();

    void showErrorMessage(View view, String msg);

    void startRefreshAnimation();

    void stopRefreshAnimation();

    void updateStory(StoryBean story);

    void updateStoryExtraInfo(StoryExtraInfoBean extraInfo);
}
