package com.example.wlbreath.zhihudaily.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wlbreath on 16/3/20.
 */
public class ThemeStory implements Serializable {
    private static final long serialVersionUID = 7927067697373309529L;

    private List<String> images;
    private int type;
    private long id;
    private String title;

    public ThemeStory() {
    }

    public ThemeStory(List<String> images, int type, long id, String title) {
        this.images = images;
        this.type = type;
        this.id = id;
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ThemeStory{" +
                "images=" + images +
                ", type=" + type +
                ", id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
