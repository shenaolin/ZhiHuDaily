package com.example.wlbreath.zhihudaily.presenter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.wlbreath.zhihudaily.bean.ThemeBean;
import com.example.wlbreath.zhihudaily.bean.ThemeStory;
import com.example.wlbreath.zhihudaily.biz.IThemeBiz;
import com.example.wlbreath.zhihudaily.bizImpl.ThemeBiz;
import com.example.wlbreath.zhihudaily.view.IThemePageView;

import java.util.List;

/**
 * Created by wlbreath on 16/4/10.
 */
public class ThemePagePresenter {
    private final String TAG = "ThemePagePresenter";

    private Context context;
    private IThemeBiz themeBiz;
    private IThemePageView themePageView;

    private boolean isLoading = false;

    private Handler handler = new Handler();

    public ThemePagePresenter(Context context, IThemePageView themePageView) {
        this.context = context;
        this.themePageView = themePageView;

        this.themeBiz = new ThemeBiz(context);
    }

    public void loadTheme(long id) {
        if (isLoading) {
            return;
        }

        themePageView.displayContentArea(true);
        themePageView.displayErrorPage(false);

        isLoading = true;

        themePageView.startRefreshAnimation();

        themeBiz.loadLatestThemes(id, new IThemeBiz.OnLoadThemeListener() {
            @Override
            public void onSuccess(final ThemeBean theme) {
                System.out.println("loadTheme: success");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;

                        themePageView.update(theme);

                        themePageView.stopRefreshAnimation();
                        themePageView.displayContentArea(true);
                        themePageView.displayErrorPage(false);
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                System.out.println("loadTheme: fail");
                e.printStackTrace();
                Log.e(TAG, e.getMessage());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;

                        themePageView.stopRefreshAnimation();
                        themePageView.displayContentArea(false);
                        themePageView.displayErrorPage(true);
                    }
                });
            }
        });
    }

    public void loadThemeStories(long themeId, long storyId) {
        if (isLoading) {
            return;
        }

        isLoading = true;

        themePageView.displayContentArea(true);
        themePageView.displayErrorPage(false);

        themePageView.startRefreshAnimation();

        themeBiz.loadHistoryThemeStories(themeId, storyId, new IThemeBiz.OnLoadThemeStoryListener() {
            @Override
            public void onSuccess(final List<ThemeStory> themeStories) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                        themePageView.appendStories(themeStories);

                        themePageView.stopRefreshAnimation();
                    }
                });
            }

            @Override
            public void onFail(final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                        themePageView.showErrorMessage(themePageView.getContentArea(), e.getMessage());

                        themePageView.stopRefreshAnimation();
                    }
                });
            }
        });

    }
}
