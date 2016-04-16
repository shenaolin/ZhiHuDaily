package com.example.wlbreath.zhihudaily.biz;

import com.example.wlbreath.zhihudaily.bean.NewsBean;
import com.example.wlbreath.zhihudaily.bean.TopNewsBean;

import java.util.Date;
import java.util.List;

/**
 * Created by wlbreath on 16/4/8.
 */
public interface INewsBiz {
    void loadLatestNews(final OnLatestNewsLoadedListener listener);

    void loadHistoryNews(final Date date, final OnHistoryNewsLoadedListener listener);

    boolean canLoadLatestNews();

    boolean canLoadHistoryNews(final Date date);

    public interface OnLatestNewsLoadedListener {
        void onSuccess(Date date, List<TopNewsBean> topNews, List<NewsBean> news);

        void onFail(Exception e);
    }

    public interface OnHistoryNewsLoadedListener {
        void onSuccess(List<NewsBean> news);

        void onFail(Exception e);
    }
}
