package com.example.wlbreath.zhihudaily.view;

import android.view.View;
import android.widget.FrameLayout;

import com.example.wlbreath.zhihudaily.bean.NewsBean;
import com.example.wlbreath.zhihudaily.bean.TopNewsBean;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wlbreath on 16/4/8.
 */
public interface INewsPageView {
    void displayContentArea(boolean show);

    void displayErrorPage(boolean show);

    void updateTopNews(Date date, List<TopNewsBean> news);

    void updateNews(LinkedHashMap<Date, List<NewsBean>> news);

    void appendNews(Date date, List<NewsBean> news);


    void showErrorMessage(View view, String msg);

    void startRefreshAnimation();

    void stopRefreshAnimation();

    FrameLayout getContentArea();

    FrameLayout getErrorPage();

}
