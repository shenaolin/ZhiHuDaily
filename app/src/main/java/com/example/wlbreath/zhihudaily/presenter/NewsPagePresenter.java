package com.example.wlbreath.zhihudaily.presenter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.wlbreath.zhihudaily.R;
import com.example.wlbreath.zhihudaily.bean.NewsBean;
import com.example.wlbreath.zhihudaily.bean.TopNewsBean;
import com.example.wlbreath.zhihudaily.biz.INewsBiz;
import com.example.wlbreath.zhihudaily.bizImpl.NewsBiz;
import com.example.wlbreath.zhihudaily.utils.NetworkUtils;
import com.example.wlbreath.zhihudaily.view.INewsPageView;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wlbreath on 16/4/8.
 */
public class NewsPagePresenter {
    private final String TAG = "NewsPagePresenter";

    private Context context;
    private INewsBiz newsBiz;
    private INewsPageView newsView;

    private boolean isLoadingNews = false;

    private Handler handler = new Handler();

    public NewsPagePresenter(Context context, INewsPageView newsView) {
        this.context = context;
        this.newsView = newsView;

        this.newsBiz = new NewsBiz(context);
    }

    public void loadLatestNews() {
        if (!newsBiz.canLoadLatestNews()) {
            newsView.displayErrorPage(false);
            newsView.displayContentArea(true);
            return;
        }

        if (isLoadingNews) {
            Log.w(TAG, "loadLatestNews: current is loading news, can not load at same time");
            return;
        }

        isLoadingNews = true;

        newsView.startRefreshAnimation();

        newsBiz.loadLatestNews(new INewsBiz.OnLatestNewsLoadedListener() {
            @Override
            public void onSuccess(final Date date, final List<TopNewsBean> topNews, final List<NewsBean> news) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLoadingNews = false;

                        LinkedHashMap<Date, List<NewsBean>> map = new LinkedHashMap<Date, List<NewsBean>>();
                        map.put(date, news);
                        newsView.updateNews(map);
                        newsView.updateTopNews(date, topNews);

                        newsView.displayErrorPage(false);
                        newsView.displayContentArea(true);

                        newsView.stopRefreshAnimation();
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());

                System.out.println("wanglei is cool and houna is cute: newsPagePresenter");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLoadingNews = false;

                        newsView.displayErrorPage(true);
                        newsView.displayContentArea(false);

                        newsView.stopRefreshAnimation();
                    }
                });
            }
        });
    }

    public void loadHistoryNews(final Date date) {
        if (!newsBiz.canLoadHistoryNews(date)) {
            newsView.showErrorMessage(newsView.getContentArea(),
                    context.getResources().getString(R.string.offline));
            return;
        }

        if (isLoadingNews) {
            Log.w(TAG, "loadHistoryNews: current is loading news, can not load at same time");
            return;
        }

        isLoadingNews = true;

        newsView.startRefreshAnimation();

        newsBiz.loadHistoryNews(date, new INewsBiz.OnHistoryNewsLoadedListener() {
            @Override
            public void onSuccess(final List<NewsBean> news) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLoadingNews = false;

                        newsView.appendNews(date, news);

                        newsView.stopRefreshAnimation();
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());

                isLoadingNews = false;

                newsView.stopRefreshAnimation();
            }
        });
    }

    public boolean isOnline() {
        return NetworkUtils.isOnline(context);
    }
}
