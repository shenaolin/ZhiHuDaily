package com.example.wlbreath.zhihudaily.biz;

import com.example.wlbreath.zhihudaily.bean.StoryBean;
import com.example.wlbreath.zhihudaily.bean.StoryExtraInfoBean;

/**
 * Created by wlbreath on 16/4/11.
 */
public interface IStoryBiz {
    void loadStory(long id, OnLoadStoryListener listener);

    void loadStoryExtraInfo(long id, OnLoadStoryExtraInfoListener listener);

    public interface OnLoadStoryListener {
        void onSuccess(StoryBean story);

        void onFail(Exception e);
    }

    public interface OnLoadStoryExtraInfoListener {
        void onSuccess(StoryExtraInfoBean extraInfo);

        void onFail(Exception e);
    }
}
