package com.example.wlbreath.zhihudaily.bizImpl;

import android.content.Context;
import android.util.Log;

import com.example.wlbreath.zhihudaily.R;
import com.example.wlbreath.zhihudaily.bean.CommentBean;
import com.example.wlbreath.zhihudaily.biz.IStoryCommentBiz;
import com.example.wlbreath.zhihudaily.utils.JsonUtils;
import com.example.wlbreath.zhihudaily.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlbreath on 16/4/16.
 */
public class StoryCommentBiz implements IStoryCommentBiz {
    private final String TAG = "StoryCommentBiz";

    private Context context;

    private List<CommentBean> longComments = new ArrayList<>();
    private List<CommentBean> shortComments = new ArrayList<>();

    public StoryCommentBiz(Context context) {
        this.context = context;
    }

    @Override
    public void loadStoryLongComments(long storyId, final OnCommentLoadListener listener) {
        loadStoryComment(getLongCommentsUrl(storyId), listener);
    }

    @Override
    public void loadStoryLongCommentsBeforeId(long storyId, long commentId, OnCommentLoadListener listener) {
        loadStoryComment(getLongCommentsBeforeIdUrl(storyId, commentId), listener);
    }

    @Override
    public void loadStoryShortCommetns(long storyId, OnCommentLoadListener listener) {
        loadStoryComment(getShortCommentsUrl(storyId), listener);
    }

    @Override
    public void loadStoryShortCommentsBeforeId(long storyId, long commentId, OnCommentLoadListener listener) {
        loadStoryComment(getShortCommentsBeforeIdUrl(storyId, commentId), listener);
    }

    private void loadStoryComment(final String url, final OnCommentLoadListener listener){
        JsonUtils jsonUtils = getJsonUtils();

        if (!NetworkUtils.isOnline(context) && !jsonUtils.isInCache(url)) {
            if (listener != null) {
                listener.onFail(new Exception(context.getResources().getString(R.string.offline)));
            }
            return;
        }

        JsonUtils.CallBack callBack = new JsonUtils.CallBack() {
            @Override
            public void success(String json) {
                if (listener != null) {
                    listener.onSuccess(parseComments(json));
                }
            }

            @Override
            public void fail(Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();

                if (listener != null) {
                    listener.onFail(e);
                }
            }

            @Override
            public void timeout() {
            }
        };

        if (NetworkUtils.isOnline(context)) {
            jsonUtils.loadJsonFromNetwork(url, "GET", callBack);

        } else {
            jsonUtils.loadJsonFromCache(url, callBack);
        }
    }

    private String getLongCommentsUrl(long storyId) {
        return context.getResources()
                .getString(R.string.story_comment_base_url) + "/" + storyId + "/long-comments";
    }

    private String getShortCommentsUrl(long storyId) {
        return context.getResources()
                .getString(R.string.story_comment_base_url) + "/" + storyId + "/short-comments";
    }

    private String getLongCommentsBeforeIdUrl(long storyId, long commentId) {
        return getLongCommentsUrl(storyId) + "/before/" + commentId;
    }

    private String getShortCommentsBeforeIdUrl(long storyId, long commentId) {
        return getShortCommentsUrl(storyId) + "/before/" + commentId;
    }

    private List<CommentBean> parseComments(String json) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        return gson.fromJson(jsonObject.get("comments"), new TypeToken<List<CommentBean>>() {
        }.getType());
    }

    private JsonUtils getJsonUtils() {
        return JsonUtils.getJsonUtils(context);
    }
}
