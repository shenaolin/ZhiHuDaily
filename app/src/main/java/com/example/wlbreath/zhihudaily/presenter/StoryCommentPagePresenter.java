package com.example.wlbreath.zhihudaily.presenter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.wlbreath.zhihudaily.bean.CommentBean;
import com.example.wlbreath.zhihudaily.bean.StoryExtraInfoBean;
import com.example.wlbreath.zhihudaily.biz.IStoryBiz;
import com.example.wlbreath.zhihudaily.biz.IStoryCommentBiz;
import com.example.wlbreath.zhihudaily.bizImpl.StoryBiz;
import com.example.wlbreath.zhihudaily.bizImpl.StoryCommentBiz;
import com.example.wlbreath.zhihudaily.view.IStoryCommentView;

import java.util.List;

/**
 * Created by wlbreath on 16/4/16.
 */
public class StoryCommentPagePresenter {
    private final String TAG = "CommentPagePresenter";

    private Context context;
    private IStoryBiz storyBiz;
    private IStoryCommentBiz storyCommentBiz;
    private IStoryCommentView storyCommentView;

    private Handler handler = new Handler();

    private boolean isLongCommentLoading  = false;
    private boolean isShortCommetnLoading = false;

    public StoryCommentPagePresenter(Context context, IStoryCommentView storyCommentView) {
        this.context = context;
        this.storyCommentView = storyCommentView;

        this.storyBiz = new StoryBiz(context);
        this.storyCommentBiz = new StoryCommentBiz(context);
    }

    public void loadStoryLongComments(long storyId) {
        if (isLongCommentLoading) {
            Log.w(TAG, "now is loading");
            return;
        }

        isLongCommentLoading = true;

        storyCommentView.displayContentArea(true);
        storyCommentView.displayErrorPage(false);

        storyCommentView.startRefreshAnimation();

        storyCommentBiz.loadStoryLongComments(storyId, new IStoryCommentBiz.OnCommentLoadListener() {
            @Override
            public void onSuccess(final List<CommentBean> comments) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLongCommentLoading = false;

                        storyCommentView.displayContentArea(true);
                        storyCommentView.displayErrorPage(false);

                        storyCommentView.updateLongComments(comments);

                        storyCommentView.stopRefreshAnimation();
                    }
                });
            }

            @Override
            public void onFail(final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLongCommentLoading = false;

                        storyCommentView.displayContentArea(false);
                        storyCommentView.displayErrorPage(true);

                        storyCommentView.showErrorMessage(storyCommentView.getErrorPage(), e.getMessage());

                        storyCommentView.stopRefreshAnimation();

                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void loadStoryLongCommentsBeforeId(long storyId, long CommentId) {
        if (isLongCommentLoading) {
            Log.w(TAG, "now is loading");
            return;
        }

        isLongCommentLoading = true;

        storyCommentView.startRefreshAnimation();

        storyCommentBiz.loadStoryLongCommentsBeforeId(storyId, CommentId, new IStoryCommentBiz.OnCommentLoadListener() {
            @Override
            public void onSuccess(final List<CommentBean> comments) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLongCommentLoading = false;

                        storyCommentView.appendLongComments(comments);

                        storyCommentView.stopRefreshAnimation();
                    }
                });
            }

            @Override
            public void onFail(final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isLongCommentLoading = false;

                        storyCommentView.showErrorMessage(storyCommentView.getContentArea(), e.getMessage());

                        storyCommentView.stopRefreshAnimation();

                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void loadStoryShortComments(long storyId) {
        if (isShortCommetnLoading) {
            Log.w(TAG, "now is loading");
            return;
        }

        isShortCommetnLoading = true;

        storyCommentView.startRefreshAnimation();

        storyCommentBiz.loadStoryShortCommetns(storyId, new IStoryCommentBiz.OnCommentLoadListener() {
            @Override
            public void onSuccess(final List<CommentBean> comments) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isShortCommetnLoading = false;

                        storyCommentView.updateShortComments(comments);

                        storyCommentView.stopRefreshAnimation();
                    }
                });
            }

            @Override
            public void onFail(final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isShortCommetnLoading = false;

                        storyCommentView.stopRefreshAnimation();

                        storyCommentView.showErrorMessage(storyCommentView.getContentArea(), e.getMessage());

                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void loadStoryShortCommentsBeforeId(long storyId, long commentId) {
        if (isShortCommetnLoading) {
            Log.w(TAG, "now is loading");
            return;
        }

        isShortCommetnLoading = true;

        storyCommentView.startRefreshAnimation();

        storyCommentBiz.loadStoryShortCommentsBeforeId(storyId, commentId, new IStoryCommentBiz.OnCommentLoadListener() {
            @Override
            public void onSuccess(final List<CommentBean> comments) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isShortCommetnLoading = false;

                        storyCommentView.appendShortComments(comments);

                        storyCommentView.stopRefreshAnimation();
                    }
                });
            }

            @Override
            public void onFail(final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isShortCommetnLoading = false;

                        storyCommentView.showErrorMessage(storyCommentView.getContentArea(), e.getMessage());

                        storyCommentView.stopRefreshAnimation();

                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public void loadCommentMeta(long storyId){
        storyCommentView.displayContentArea(true);
        storyCommentView.displayErrorPage(false);

        storyBiz.loadStoryExtraInfo(storyId, new IStoryBiz.OnLoadStoryExtraInfoListener() {
            @Override
            public void onSuccess(final StoryExtraInfoBean extraInfo) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int longCommentCount  = extraInfo.getLong_comments();
                        int shortCommentCount = extraInfo.getShort_comments();

                        storyCommentView.updateTitle(longCommentCount, shortCommentCount);
                        storyCommentView.uploadCommentCount(longCommentCount, shortCommentCount);

                        storyCommentView.displayContentArea(true);
                        storyCommentView.displayErrorPage(false);
                    }
                });
            }

            @Override
            public void onFail(final Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        e.printStackTrace();
                        storyCommentView.displayContentArea(false);
                        storyCommentView.displayErrorPage(true);

                        storyCommentView.showErrorMessage(storyCommentView.getErrorPage(), e.getMessage());
                    }
                });
            }
        });
    }
}
