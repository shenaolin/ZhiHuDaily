package com.example.wlbreath.zhihudaily.bean;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by wlbreath on 16/3/6.
 */
public class ThemeMenuBean {
    private int limit;
    private List<ThemeCategoryBean> subscribed;
    private List<ThemeCategoryBean> others;

    public ThemeMenuBean() {
    }

    public ThemeMenuBean(int limit, List<ThemeCategoryBean> subscribed, List<ThemeCategoryBean> others) {
        this.subscribed = subscribed;
        this.limit = limit;
        this.others = others;
    }

    public static ThemeMenuBean getThemeMenuBeanFromJson(String json) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();

        JsonObject object = jsonParser.parse(json).getAsJsonObject();

        int limit = gson.fromJson(object.get("limit"), int.class);

        List<ThemeCategoryBean> subscribed = gson.fromJson(object.get("subscribed"),
                new TypeToken<List<ThemeCategoryBean>>() {
                }.getType());

        List<ThemeCategoryBean> others = gson.fromJson(object.get("others"),
                new TypeToken<List<ThemeCategoryBean>>() {
                }.getType());

        return new ThemeMenuBean(limit, subscribed, others);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<ThemeCategoryBean> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(List<ThemeCategoryBean> subscribed) {
        this.subscribed = subscribed;
    }

    public List<ThemeCategoryBean> getOthers() {
        return others;
    }

    public void setOthers(List<ThemeCategoryBean> others) {
        this.others = others;
    }

    public int getSubscribedCount() {
        return getSubscribed().size();
    }

    public int getOthersCount() {
        return getOthers().size();
    }

    public ThemeCategoryBean getThemeBean(int position) {
        ThemeCategoryBean themeCategoryBean = null;
        if (position < getSubscribedCount()) {
            themeCategoryBean = subscribed.get(position);
        } else {
            themeCategoryBean = others.get(position - getSubscribedCount());
        }
        return themeCategoryBean;
    }

    @Override
    public String toString() {
        String info = "limit: " + limit + "\r\n";

        for (ThemeCategoryBean bean : others) {
            info += bean.toString() + "\r\n";
        }

        return info;
    }
}
