package com.example.wlbreath.zhihudaily.view;

import android.view.View;
import android.view.ViewGroup;

import com.example.wlbreath.zhihudaily.bean.CommentBean;

import java.util.List;

/**
 * Created by wlbreath on 16/4/16.
 */
public interface IStoryCommentView {
    void displayContentArea(boolean show);

    void displayErrorPage(boolean show);

    ViewGroup getContentArea();

    ViewGroup getErrorPage();

    void showErrorMessage(View view, String msg);

    void startRefreshAnimation();

    void stopRefreshAnimation();

    void updateLongComments(List<CommentBean> comments);

    void updateShortComments(List<CommentBean> comments);

    void appendLongComments(List<CommentBean> comments);

    void appendShortComments(List<CommentBean> comments);

    void updateTitle(int longCommentCount, int shortCommentCount);

    void uploadCommentCount(int longCommentCount, int shortCommentCount );
}
