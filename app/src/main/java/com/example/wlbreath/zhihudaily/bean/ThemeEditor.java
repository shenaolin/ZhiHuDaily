package com.example.wlbreath.zhihudaily.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by wlbreath on 16/3/20.
 */
public class ThemeEditor implements Serializable {
    private static final long serialVersionUID = -557734313065258242L;

    private long id;
    private String avatar;
    private String name;

    public ThemeEditor() {
    }

    public ThemeEditor(long id, String avatar, String name) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ThemeEditor{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
