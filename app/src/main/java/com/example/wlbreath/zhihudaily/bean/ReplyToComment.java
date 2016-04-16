package com.example.wlbreath.zhihudaily.bean;

/**
 * Created by wlbreath on 16/3/25.
 */
public class ReplyToComment {
    private long id;
    private int type;
    private String content;
    private String author;
    private boolean isExpend = false;

    public ReplyToComment() {
    }

    public ReplyToComment(long id, int type, String content, String author) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isExpend() {
        return isExpend;
    }

    public void setIsExpend(boolean isExpend) {
        this.isExpend = isExpend;
    }

    @Override
    public String toString() {
        return "ReplyToComment{" +
                "id=" + id +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", isExpend=" + isExpend +
                '}';
    }
}
