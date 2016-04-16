package com.example.wlbreath.zhihudaily.bean;

import android.graphics.Bitmap;

/**
 * Created by wlbreath on 16/4/7.
 */
public class WelcomePageBean {
    private String title;
    private Bitmap background;

    public WelcomePageBean() {
    }

    public WelcomePageBean(String title, Bitmap background) {
        this.title = title;
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    @Override
    public String toString() {
        return "WelcomePageBean{" +
                "title='" + title + '\'' +
                ", background=" + background +
                '}';
    }
}
