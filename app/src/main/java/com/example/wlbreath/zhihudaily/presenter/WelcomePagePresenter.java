package com.example.wlbreath.zhihudaily.presenter;

import android.content.Context;

import com.example.wlbreath.zhihudaily.bean.WelcomePageBean;
import com.example.wlbreath.zhihudaily.biz.IWelcomePageBiz;
import com.example.wlbreath.zhihudaily.bizImpl.WelcomePageBiz;
import com.example.wlbreath.zhihudaily.view.IWelcomePageView;

/**
 * Created by wlbreath on 16/4/7.
 */
public class WelcomePagePresenter {
    private Context context;
    private IWelcomePageBiz welcomePageBiz;
    private IWelcomePageView welcomePageView;

    public WelcomePagePresenter(Context context, IWelcomePageView welcomePageView) {
        this.context = context;
        this.welcomePageView = welcomePageView;
        this.welcomePageBiz = new WelcomePageBiz(context);
    }

    public WelcomePageBean getWelcomePageBean() {
        WelcomePageBean bean = welcomePageBiz.getWelcomePageBean();

        updateCachedWelcomePageInfo();
        return bean;
    }

    private void updateCachedWelcomePageInfo() {
        welcomePageBiz.updateCachedWelcomePageInfo();
    }
}
