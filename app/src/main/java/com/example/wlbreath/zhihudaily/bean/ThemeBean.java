package com.example.wlbreath.zhihudaily.bean;

import java.util.List;

/**
 * Created by wlbreath on 16/4/10.
 */
public class ThemeBean {
    private String description;
    private String background;
    private String color;
    private String name;
    private String image;
    private String image_source;
    private List<ThemeStory> stories;
    private List<ThemeEditor> editors;

    public ThemeBean() {
    }

    public ThemeBean(String description, String background, String color, String name, String image, String image_source, List<ThemeStory> stories, List<ThemeEditor> editors) {
        this.description = description;
        this.background = background;
        this.color = color;
        this.name = name;
        this.image = image;
        this.image_source = image_source;
        this.stories = stories;
        this.editors = editors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public List<ThemeStory> getStories() {
        return stories;
    }

    public void setStories(List<ThemeStory> stories) {
        this.stories = stories;
    }

    public List<ThemeEditor> getEditors() {
        return editors;
    }

    public void setEditors(List<ThemeEditor> editors) {
        this.editors = editors;
    }

    @Override
    public String toString() {
        return "ThemeBean{" +
                "description='" + description + '\'' +
                ", background='" + background + '\'' +
                ", color='" + color + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", image_source='" + image_source + '\'' +
                ", stories=" + stories +
                ", editors=" + editors +
                '}';
    }
}
