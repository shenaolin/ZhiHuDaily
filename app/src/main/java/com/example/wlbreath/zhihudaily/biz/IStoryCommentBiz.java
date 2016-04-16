package com.example.wlbreath.zhihudaily.biz;

import com.example.wlbreath.zhihudaily.bean.CommentBean;

import java.util.List;

/**
 * Created by wlbreath on 16/4/16.
 */
public interface IStoryCommentBiz {
    void loadStoryLongComments(final long storyId, final OnCommentLoadListener listener);

    void loadStoryLongCommentsBeforeId(final long storyId, final long commentId, final OnCommentLoadListener listener);

    void loadStoryShortCommetns(final long storyId, final OnCommentLoadListener listener);

    void loadStoryShortCommentsBeforeId(final long storyId, final long commentId, final OnCommentLoadListener listener);

    public interface OnCommentLoadListener {

        void onSuccess(List<CommentBean> comments);

        void onFail(Exception e);
    }
}
