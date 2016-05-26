package com.example.wlbreath.zhihudaily.bean;

/**
 * Created by wlbreath on 16/3/10.
 */
public class StorySectionBean {
    private String thumbnail;
    private long id;
    private String name;

    public StorySectionBean() {
    }

    public StorySectionBean(String thumbnail, long id, String name) {
        this.thumbnail = thumbnail;
        this.id = id;
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    @Override
    public String toString() {
        return "StorySectionBean{" +
                "thumbnail='" + thumbnail + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
