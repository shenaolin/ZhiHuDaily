package com.example.wlbreath.zhihudaily.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by wlbreath on 16/3/13.
 */
public class LatestNewsBean {
    private Date date;
    private List<NewsBean> stories;
    private List<TopNewsBean> top_stories;

    public LatestNewsBean() {
    }

    public LatestNewsBean(Date date, List<NewsBean> stories, List<TopNewsBean> top_stories) {
        this.date = date;
        this.stories = stories;
        this.top_stories = top_stories;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<NewsBean> getStories() {
        return stories;
    }

    public void setStories(List<NewsBean> stories) {
        this.stories = stories;
    }

    public List<TopNewsBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopNewsBean> top_stories) {
        this.top_stories = top_stories;
    }

    @Override
    public String toString() {
        return "LatestNewsBean{" +
                "date=" + date +
                ", stories=" + stories +
                ", top_stories=" + top_stories +
                '}';
    }
}
