package com.example.wlbreath.zhihudaily.bean;

import java.util.List;

/**
 * Created by wlbreath on 16/3/7.
 */
public class NewsBean {
    private String title;
    private String ga_prefix;
    private boolean multipic;
    private int type;
    private long id;
    private List<String> images;

    public NewsBean() {
    }

    public NewsBean(String title, String ga_prefix, boolean multipic, int type, long id, List<String> images) {
        this.title = title;
        this.ga_prefix = ga_prefix;
        this.multipic = multipic;
        this.type = type;
        this.id = id;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public boolean isMultipic() {
        return multipic;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "title='" + title + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", multipic=" + multipic +
                ", type=" + type +
                ", id=" + id +
                ", images=" + images +
                '}';
    }
}
