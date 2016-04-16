package com.example.wlbreath.zhihudaily.view;

import com.example.wlbreath.zhihudaily.bean.ThemeMenuBean;

/**
 * Created by wlbreath on 16/4/8.
 */
public interface IMainPageView {
    void updateThemeMenu(ThemeMenuBean themeMenu);

    void displayErrorPage(boolean show);
    void displayContentArea(boolean show);

}
