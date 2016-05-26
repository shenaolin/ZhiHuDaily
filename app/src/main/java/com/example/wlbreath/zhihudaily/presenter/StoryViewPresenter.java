package com.example.wlbreath.zhihudaily.presenter;

import android.content.Context;
import android.os.Handler;

import com.example.wlbreath.zhihudaily.bean.StoryBean;
import com.example.wlbreath.zhihudaily.bean.StoryExtraInfoBean;
import com.example.wlbreath.zhihudaily.biz.IStoryBiz;
import com.example.wlbreath.zhihudaily.bizImpl.StoryBiz;
import com.example.wlbreath.zhihudaily.view.IStoryView;

/**
 * Created by wlbreath on 16/4/11.
 */
public class StoryViewPresenter {
    private final String TAG = "StoryViewPresenter";

    private Context context;
    private IStoryBiz storyBiz;
    private IStoryView storyView;

    private Handler handler = new Handler();

    public StoryViewPresenter(Context context, IStoryView storyView) {
        this.context = context;
        this.storyView = storyView;

        this.storyBiz = new StoryBiz(context);
    }

    public void loadStory(long id) {
        storyView.displayErrorPage(false);
        storyView.displayContentArea(true);
        storyView.startRefreshAnimation();

        storyBiz.loadStory(id, new IStoryBiz.OnLoadStoryListener() {
            @Override
            public void onSuccess(final StoryBean story) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        storyView.updateStory(story);

                        storyView.displayErrorPage(false);
                        storyView.displayContentArea(true);
                        storyView.stopRefreshAnimation();
                    }
                });
            }

            @Override
            public void onFail(final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        storyView.displayErrorPage(false);
                        storyView.displayContentArea(true);
                        storyView.stopRefreshAnimation();

                        storyView.showErrorMessage(storyView.getErrorPage(), e.getMessage());
                    }
                });
            }
        });
    }

    public void loadStoryExtraInfo(long id) {
        storyBiz.loadStoryExtraInfo(id, new IStoryBiz.OnLoadStoryExtraInfoListener() {
            @Override
            public void onSuccess(final StoryExtraInfoBean extraInfo) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        storyView.updateStoryExtraInfo(extraInfo);
                    }
                });
            }

            @Override
            public void onFail(final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        storyView.showErrorMessage(storyView.getContentArea(), e.getMessage());
                    }
                });
            }
        });
    }
}
