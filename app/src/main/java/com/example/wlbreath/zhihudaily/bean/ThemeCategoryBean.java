package com.example.wlbreath.zhihudaily.bean;

import java.io.Serializable;

/**
 * Created by wlbreath on 16/3/6.
 */
public class ThemeCategoryBean implements Serializable {
    private static final long serialVersionUID = 5810068708553608324L;

    private int color;
    private String thumbnail;
    private String description;
    private long id;
    private String name;

    public ThemeCategoryBean() {
    }

    public ThemeCategoryBean(int color, String thumbnail, String description, long id, String name) {
        this.color = color;
        this.thumbnail = thumbnail;
        this.description = description;
        this.id = id;
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "ThemeCategoryBean{" +
                "color=" + color +
                ", thumbnail='" + thumbnail + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
