package com.example.wlbreath.zhihudaily.bean;

/**
 * Created by wlbreath on 16/3/11.
 */
public class StoryExtraInfoBean {
    private int post_reasons;
    private int long_comments;
    private int popularity;
    private int normal_comments;
    private int comments;
    private int short_comments;

    public StoryExtraInfoBean() {
    }

    public StoryExtraInfoBean(int post_reasons, int long_comments, int popularity, int normal_comments, int comments, int short_comments) {
        this.post_reasons = post_reasons;
        this.long_comments = long_comments;
        this.popularity = popularity;
        this.normal_comments = normal_comments;
        this.comments = comments;
        this.short_comments = short_comments;
    }

    public int getPost_reasons() {
        return post_reasons;
    }

    public void setPost_reasons(int post_reasons) {
        this.post_reasons = post_reasons;
    }

    public int getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(int long_comments) {
        this.long_comments = long_comments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getNormal_comments() {
        return normal_comments;
    }

    public void setNormal_comments(int normal_comments) {
        this.normal_comments = normal_comments;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    @Override
    public String toString() {
        return "StoryExtraInfoBean{" +
                "post_reasons=" + post_reasons +
                ", long_comments=" + long_comments +
                ", popularity=" + popularity +
                ", normal_comments=" + normal_comments +
                ", comments=" + comments +
                ", short_comments=" + short_comments +
                '}';
    }
}
