package com.example.wlbreath.zhihudaily.bizImpl;

import android.content.Context;

import com.example.wlbreath.zhihudaily.R;
import com.example.wlbreath.zhihudaily.bean.TopNewsBean;
import com.example.wlbreath.zhihudaily.bean.NewsBean;
import com.example.wlbreath.zhihudaily.biz.INewsBiz;
import com.example.wlbreath.zhihudaily.utils.JsonUtils;
import com.example.wlbreath.zhihudaily.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wlbreath on 16/4/8.
 */
public class NewsBiz implements INewsBiz {
    private Context context;

    public NewsBiz(Context context) {
        this.context = context;
    }

    @Override
    public void loadLatestNews(final OnLatestNewsLoadedListener listener) {
        JsonUtils jsonUtils = getJsonUtils();
        String jsonUrl = getLatestNewsJsonUrl();

        if (!canLoadLatestNews()) {
            listener.onFail(new Exception("offine line can not load latest news: " + jsonUrl));
            return;
        }

        JsonUtils.CallBack callBack = new JsonUtils.CallBack() {
            @Override
            public void success(String json) {
                if (listener != null) {
                    Date date = parseLatestNewsDate(json);
                    List<NewsBean> news = parseLatestNewsNews(json);
                    List<TopNewsBean> topNews = parseLastestNewsTopNews(json);

                    listener.onSuccess(date, topNews, news);
                }
            }

            @Override
            public void fail(Exception e) {
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
    public void loadHistoryNews(Date date, final OnHistoryNewsLoadedListener listener) {
        JsonUtils jsonUtils = getJsonUtils();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, +24);
        String jsonUrl = getHistoryNewsJsonUrl(cal.getTime());

        if (!canLoadHistoryNews(date)) {
            if (listener != null) {
                listener.onFail(new Exception("offline can not load history news: " + jsonUrl));
            }
            return;
        }

        JsonUtils.CallBack callBack = new JsonUtils.CallBack() {
            @Override
            public void success(String json) {
                if (listener != null) {
                    listener.onSuccess(parseHistoryNews(json));
                }
            }

            @Override
            public void fail(Exception e) {
                if (listener != null) {
                    listener.onFail(e);
                }
            }

            @Override
            public void timeout() {
            }
        };

        if (jsonUtils.isInCache(jsonUrl)) {
            jsonUtils.loadJsonFromCache(jsonUrl, callBack);

        } else {
            jsonUtils.loadJsonFromNetwork(jsonUrl, "GET", callBack);
        }
    }

    @Override
    public boolean canLoadLatestNews() {
        JsonUtils jsonUtils = getJsonUtils();
        String jsonUrl = getLatestNewsJsonUrl();

        return NetworkUtils.isOnline(context) || jsonUtils.isInCache(jsonUrl);
    }

    @Override
    public boolean canLoadHistoryNews(Date date) {
        JsonUtils jsonUtils = getJsonUtils();
        String jsonUrl = getHistoryNewsJsonUrl(date);

        return NetworkUtils.isOnline(context) || jsonUtils.isInCache(jsonUrl);
    }

    private Date parseLatestNewsDate(String json) {
        Date date = null;

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        try {
            String dateString = gson.fromJson(jsonObject.get("date"), String.class);
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    private List<TopNewsBean> parseLastestNewsTopNews(String json) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        return gson.fromJson(jsonObject.get("top_stories"), new TypeToken<List<TopNewsBean>>() {
        }.getType());
    }

    private List<NewsBean> parseLatestNewsNews(String json) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        return gson.fromJson(jsonObject.get("stories"), new TypeToken<List<NewsBean>>() {
        }.getType());
    }

    private List<NewsBean> parseHistoryNews(String json) {
        Gson gson = new Gson();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        return gson.fromJson(jsonObject.get("stories"), new TypeToken<List<NewsBean>>() {
        }.getType());
    }

    private String getLatestNewsJsonUrl() {
        return context.getResources().getString(R.string.latest_news_url);
    }

    private String getHistoryNewsJsonUrl(Date date) {
        String baseUrl = context.getResources().getString(R.string.history_news_base_url);

        return baseUrl + "/" + new SimpleDateFormat("yyyyMMdd").format(date);
    }

    private JsonUtils getJsonUtils() {
        return JsonUtils.getJsonUtils(context);
    }
}
