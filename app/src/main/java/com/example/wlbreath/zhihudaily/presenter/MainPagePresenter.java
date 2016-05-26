package com.example.wlbreath.zhihudaily.presenter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.wlbreath.zhihudaily.bean.ThemeMenuBean;
import com.example.wlbreath.zhihudaily.biz.IThemeBiz;
import com.example.wlbreath.zhihudaily.bizImpl.ThemeBiz;
import com.example.wlbreath.zhihudaily.view.IMainPageView;

/**
 * Created by wlbreath on 16/4/8.
 */
public class MainPagePresenter {
    private final String TAG = "MainPagePresenter";

    private final int UPDATE_THEMES_SUCCSSS = 0;
    private final int UPDATE_THEMES_FAIL = 0;

    private Context context;
    private IThemeBiz themeBiz;
    private IMainPageView mainPageView;

    private Handler handler = new Handler();

    public MainPagePresenter(Context context, IMainPageView mainPageView) {
        this.context = context;
        this.mainPageView = mainPageView;

        this.themeBiz = new ThemeBiz(context);
    }

    public void updateThemeMenu() {
        themeBiz.loadThemeCategories(new IThemeBiz.OnLoadThemeMenuListener() {
            @Override
            public void onSuccess(final ThemeMenuBean themeMenu) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mainPageView.displayErrorPage(false);
                        mainPageView.displayContentArea(true);
                        mainPageView.updateThemeMenu(themeMenu);
                    }
                });

            }

            @Override
            public void onFail(final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mainPageView.displayErrorPage(true);
                        mainPageView.displayContentArea(false);

                        Log.e(TAG, e.getMessage());
                    }
                });

            }
        });
    }
}
