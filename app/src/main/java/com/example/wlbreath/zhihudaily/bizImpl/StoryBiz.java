package com.example.wlbreath.zhihudaily.bizImpl;

import android.content.Context;
import android.util.Log;

import com.example.wlbreath.zhihudaily.R;
import com.example.wlbreath.zhihudaily.bean.RecommenderBean;
import com.example.wlbreath.zhihudaily.bean.StoryBean;
import com.example.wlbreath.zhihudaily.bean.StoryExtraInfoBean;
import com.example.wlbreath.zhihudaily.bean.StorySectionBean;
import com.example.wlbreath.zhihudaily.biz.IStoryBiz;
import com.example.wlbreath.zhihudaily.utils.JsonUtils;
import com.example.wlbreath.zhihudaily.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wlbreath on 16/4/11.
 */
public class StoryBiz implements IStoryBiz {
    private final String TAG = "StoryBiz";

    private Context context;

    public StoryBiz(Context context) {
        this.context = context;
    }

    @Override
    public void loadStory(long id, final OnLoadStoryListener listener) {
        JsonUtils jsonUtils = getJsonUtils();
        String storyUrl = getStoryUrl(id);

        if (!NetworkUtils.isOnline(context) && !jsonUtils.isInCache(storyUrl)) {
            if (listener != null) {
                listener.onFail(new Exception(context.getResources().getString(R.string.offline)));
            }
            return;
        }

        jsonUtils.loadJson(storyUrl, "GET", new JsonUtils.CallBack() {
            @Override
            public void success(String json) {
                if (listener != null) {
                    listener.onSuccess(parseStory(json));
                }
            }

            @Override
            public void fail(Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());

                if (listener != null) {
                    listener.onFail(e);
                }
            }

            @Override
            public void timeout() {
            }
        });
    }

    private StoryBean parseStory(String json) {
        StoryBean infoBean = new StoryBean();

        Gson gson = new Gson();

        Type listStringType = new TypeToken<List<String>>() {
        }.getType();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        infoBean.setBody(gson.fromJson(jsonObject.get("body"), String.class));
        infoBean.setImageSource(gson.fromJson(jsonObject.get("image_source"), String.class));
        infoBean.setTitle(gson.fromJson(jsonObject.get("title"), String.class));
        infoBean.setImage(gson.fromJson(jsonObject.get("image"), String.class));
        infoBean.setShare_url(gson.fromJson(jsonObject.get("share_url"), String.class));
        infoBean.setJs((List<String>) gson.fromJson(jsonObject.get("js"), listStringType));
        infoBean.setGa_prefix(gson.fromJson(jsonObject.get("ga_prefix"), String.class));
        infoBean.setType(gson.fromJson(jsonObject.get("type"), int.class));
        infoBean.setId(gson.fromJson(jsonObject.get("id"), long.class));
        infoBean.setCss((List<String>) gson.fromJson(jsonObject.get("css"), listStringType));
        infoBean.setRecommenders((List<RecommenderBean>) gson.fromJson(
                jsonObject.get("recommenders"), new TypeToken<List<RecommenderBean>>() {
                }.getType()));
        infoBean.setSection(gson.fromJson(jsonObject.get("section"), StorySectionBean.class));

        return infoBean;
    }

    @Override
    public void loadStoryExtraInfo(long id, final OnLoadStoryExtraInfoListener listener) {
        JsonUtils jsonUtils = getJsonUtils();
        String storyExtraInfoUrl = getStoryExtraInfoUrl(id);

        if (!NetworkUtils.isOnline(context) && !jsonUtils.isInCache(storyExtraInfoUrl)) {
            if (listener != null) {
                listener.onFail(new Exception(context.getResources().getString(R.string.offline)));
            }
            return;
        }

        JsonUtils.CallBack callBack = new JsonUtils.CallBack() {
            @Override
            public void success(String json) {
                if (listener != null) {
                    listener.onSuccess(parseStoryExtraInfo(json));
                }
            }

            @Override
            public void fail(Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());

                if (listener != null) {
                    listener.onFail(e);
                }
            }

            @Override
            public void timeout() {
            }
        };

        if (NetworkUtils.isOnline(context)) {
            jsonUtils.loadJsonFromNetwork(storyExtraInfoUrl, "GET", callBack);

        } else {
            jsonUtils.loadJsonFromCache(storyExtraInfoUrl, callBack);
        }
    }

    private StoryExtraInfoBean parseStoryExtraInfo(String json) {
        return new Gson().fromJson(json, StoryExtraInfoBean.class);
    }

    private String getStoryUrl(long id) {
        return context.getResources().getString(R.string.story_base_url) + "/" + id;
    }

    private String getStoryExtraInfoUrl(long id) {
        return context.getResources().getString(R.string.story_extra_info_base_url) + "/" + id;
    }

    private JsonUtils getJsonUtils() {
        return JsonUtils.getJsonUtils(context);
    }
}
