package com.example.wlbreath.zhihudaily.bean;

import java.util.List;

/**
 * Created by wlbreath on 16/3/9.
 */
public class StoryBean {
    private String body;
    private String imageSource;
    private String title;
    private String image;
    private String share_url;
    private List<String> js;
    private String ga_prefix;
    private int type;
    private long id;
    private List<String> css;
    private List<RecommenderBean> recommenders;
    private StorySectionBean section;

    public StoryBean() {
    }

    public StoryBean(String body, String imageSource, String title, String image, String share_url, List<String> js, String ga_prefix, int type, long id, List<String> css) {
        this.body = body;
        this.imageSource = imageSource;
        this.title = title;
        this.image = image;
        this.share_url = share_url;
        this.js = js;
        this.ga_prefix = ga_prefix;
        this.type = type;
        this.id = id;
        this.css = css;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public List<String> getJs() {
        return js;
    }

    public void setJs(List<String> js) {
        this.js = js;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
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

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public List<RecommenderBean> getRecommenders() {
        return recommenders;
    }

    public void setRecommenders(List<RecommenderBean> recommenders) {
        this.recommenders = recommenders;
    }

    public StorySectionBean getSection() {
        return section;
    }

    public void setSection(StorySectionBean section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "StoryBean{" +
                "body='" + body + '\'' +
                ", imageSource='" + imageSource + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", share_url='" + share_url + '\'' +
                ", js=" + js +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", type=" + type +
                ", id=" + id +
                ", css=" + css +
                ", recommenders=" + recommenders +
                ", section=" + section +
                '}';
    }
}
