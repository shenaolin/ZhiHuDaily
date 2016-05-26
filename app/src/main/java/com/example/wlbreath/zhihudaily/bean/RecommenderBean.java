package com.example.wlbreath.zhihudaily.bean;

/**
 * Created by wlbreath on 16/3/10.
 */
public class RecommenderBean {
    private String avatar;

    public RecommenderBean() {
    }

    public RecommenderBean(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "RecommenderBean{" +
                "avatar='" + avatar + '\'' +
                '}';
    }
}
