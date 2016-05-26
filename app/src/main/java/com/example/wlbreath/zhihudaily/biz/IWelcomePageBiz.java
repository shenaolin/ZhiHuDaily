package com.example.wlbreath.zhihudaily.biz;

import com.example.wlbreath.zhihudaily.bean.WelcomePageBean;

/**
 * Created by wlbreath on 16/4/7.
 */
public interface IWelcomePageBiz {
    WelcomePageBean getWelcomePageBean();

    void updateCachedWelcomePageInfo();
}
