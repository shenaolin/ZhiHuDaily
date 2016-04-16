package com.example.wlbreath.zhihudaily.bizImpl;

import android.content.Context;
import android.util.Log;

import com.example.wlbreath.zhihudaily.R;
import com.example.wlbreath.zhihudaily.bean.ThemeBean;
import com.example.wlbreath.zhihudaily.bean.ThemeCategoryBean;
import com.example.wlbreath.zhihudaily.bean.ThemeEditor;
import com.example.wlbreath.zhihudaily.bean.ThemeMenuBean;
import com.example.wlbreath.zhihudaily.bean.ThemeStory;
import com.example.wlbreath.zhihudaily.biz.IThemeBiz;
import com.example.wlbreath.zhihudaily.utils.JsonUtils;
import com.example.wlbreath.zhihudaily.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by wlbreath on 16/4/8.
 */
public class ThemeBiz implements IThemeBiz {
    private final String TAG = "ThemeBiz";

    private Context context;

    public ThemeBiz(Context context) {
        this.context = context;
    }

    @Override
    public void loadThemeCategories(final OnLoadThemeMenuListener listener) {
        JsonUtils jsonUtils = getJsonUtils();
        String themesJsonUrl = getThemeCategoriesUrl();

        if (!jsonUtils.isInCache(themesJsonUrl) && !NetworkUtils.isOnline(context)) {
            listener.onFail(new Exception("offline, can not load themes"));
            return;
        }

        JsonUtils.CallBack callBack = new JsonUtils.CallBack() {
            @Override
            public void success(String json) {
                listener.onSuccess(parseThemeCategories(json));
            }

            @Override
            public void fail(Exception e) {
                listener.onFail(e);
            }

            @Override
            public void timeout() {
                listener.onFail(new Exception("load themes timeout"));
            }
        };

        if (jsonUtils.isInCache(themesJsonUrl)) {
            jsonUtils.loadJsonFromCache(themesJsonUrl, callBack);

            // update the cached themes
            if (NetworkUtils.isOnline(context)) {
                jsonUtils.loadJsonFromNetwork(themesJsonUrl, "GET", null);
            }

        } else {
            jsonUtils.loadJsonFromNetwork(themesJsonUrl, "GET", callBack);
        }
    }

    @Override
    public void loadLatestThemes(long id, final OnLoadThemeListener listener) {
        JsonUtils jsonUtils = getJsonUtils();
        String jsonUrl = getThemeUrl(id);

        if (!NetworkUtils.isOnline(context) && !jsonUtils.isInCache(jsonUrl)) {
            if (listener != null) {
                listener.onFail(new Exception(context.getResources().getString(R.string.offline)));
            }
            return;
        }

        JsonUtils.CallBack callBack = new JsonUtils.CallBack() {
            @Override
            public void success(String json) {
                if (listener != null) {
                    listener.onSuccess(parseTheme(json));
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
            jsonUtils.loadJsonFromNetwork(jsonUrl, "GET", callBack);

        } else {
            jsonUtils.loadJsonFromCache(jsonUrl, callBack);
        }
    }

    @Override
    public void loadHistoryThemeStories(long themeId, long storyId, final OnLoadThemeStoryListener listener) {
        JsonUtils jsonUtils = getJsonUtils();
        String jsonUrl = getHistoryThemeStoryUrl(themeId, storyId);

        if (!NetworkUtils.isOnline(context) && !jsonUtils.isInCache(jsonUrl)) {
            if (listener != null) {
                listener.onFail(new Exception(context.getResources().getString(R.string.offline)));
            }
            return;
        }

        JsonUtils.CallBack callBack = new JsonUtils.CallBack() {
            @Override
            public void success(String json) {
                if (listener != null) {
                    listener.onSuccess(parseHistoryThemeStory(json));
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
            jsonUtils.loadJsonFromNetwork(jsonUrl, "GET", callBack);

        } else {
            jsonUtils.loadJsonFromCache(jsonUrl, callBack);
        }
    }

    private ThemeMenuBean parseThemeCategories(String json) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();

        JsonObject obj = jsonParser.parse(json).getAsJsonObject();

        int limit = gson.fromJson(obj.get("limit"), int.class);

        List<ThemeCategoryBean> subscribed = gson.fromJson(obj.get("subscribed"),
                new TypeToken<List<ThemeCategoryBean>>() {
                }.getType());

        List<ThemeCategoryBean> others = gson.fromJson(obj.get("others"),
                new TypeToken<List<ThemeCategoryBean>>() {
                }.getType());

        return new ThemeMenuBean(limit, subscribed, others);
    }

    private ThemeBean parseTheme(String json) {
        ThemeBean theme = new ThemeBean();

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();

        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        theme.setDescription(gson.fromJson(jsonObject.get("description"), String.class));
        theme.setBackground(gson.fromJson(jsonObject.get("background"), String.class));
        theme.setColor(gson.fromJson(jsonObject.get("color"), String.class));
        theme.setName(gson.fromJson(jsonObject.get("name"), String.class));
        theme.setImage(gson.fromJson(jsonObject.get("image"), String.class));
        theme.setImage_source(gson.fromJson(jsonObject.get("image_source"), String.class));
        theme.setEditors(gson.<List<ThemeEditor>>fromJson(jsonObject.get("editors"),
                new TypeToken<List<ThemeEditor>>() {
                }.getType()));
        theme.setStories(gson.<List<ThemeStory>>fromJson(jsonObject.get("stories"),
                new TypeToken<List<ThemeStory>>() {
                }.getType()));
        return theme;
    }

    private List<ThemeStory> parseHistoryThemeStory(String json) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();

        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        return gson.fromJson(jsonObject.get("stories"), new TypeToken<List<ThemeStory>>() {
        }.getType());
    }

    private String getThemeCategoriesUrl() {
        return context.getResources().getString(R.string.theme_category_url);
    }

    private String getThemeUrl(long id) {
        return context.getResources().getString(R.string.theme_base_url) + "/" + id;
    }

    private String getHistoryThemeStoryUrl(long themeId, long storyId) {
        return getThemeUrl(themeId) + "/before/" + storyId;
    }

    private JsonUtils getJsonUtils() {
        return JsonUtils.getJsonUtils(context);
    }
}
