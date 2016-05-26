package com.example.wlbreath.zhihudaily.biz;

import com.example.wlbreath.zhihudaily.bean.ThemeBean;
import com.example.wlbreath.zhihudaily.bean.ThemeMenuBean;
import com.example.wlbreath.zhihudaily.bean.ThemeStory;

import java.util.List;

/**
 * Created by wlbreath on 16/4/8.
 */
public interface IThemeBiz {
    void loadThemeCategories(final OnLoadThemeMenuListener listener);

    void loadLatestThemes(long id, OnLoadThemeListener listener);

    void loadHistoryThemeStories(long themeId, long storyId, OnLoadThemeStoryListener listener);

    public interface OnLoadThemeMenuListener {
        void onSuccess(final ThemeMenuBean themeMenu);

        void onFail(Exception e);
    }

    public interface OnLoadThemeListener {
        void onSuccess(final ThemeBean theme);

        void onFail(Exception e);
    }

    public interface OnLoadThemeStoryListener {
        void onSuccess(final List<ThemeStory> themeStories);

        void onFail(Exception e);
    }
}
