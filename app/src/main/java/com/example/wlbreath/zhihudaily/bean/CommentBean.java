package com.example.wlbreath.zhihudaily.bean;

/**
 * Created by wlbreath on 16/3/25.
 */
public class CommentBean {
    private long id;
    private String author;
    private String content;
    private int likes;
    private long time;
    private String avatar;
    private ReplyToComment reply_to;
    private boolean isExpend = false;

    public CommentBean() {
    }

    public CommentBean(long id, String author, String content, int likes, long time, String avatar, ReplyToComment reply_to) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.likes = likes;
        this.time = time;
        this.avatar = avatar;
        this.reply_to = reply_to;
    }

    public CommentBean(long id, String author, String content, int likes, long time, String avatar, ReplyToComment reply_to, boolean isExpend) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.likes = likes;
        this.time = time;
        this.avatar = avatar;
        this.reply_to = reply_to;
        this.isExpend = isExpend;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ReplyToComment getReply_to() {
        return reply_to;
    }

    public void setReply_to(ReplyToComment reply_to) {
        this.reply_to = reply_to;
    }

    public boolean isExpend() {
        return isExpend;
    }

    public void setIsExpend(boolean isExpend) {
        this.isExpend = isExpend;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", likes=" + likes +
                ", time=" + time +
                ", avatar='" + avatar + '\'' +
                ", reply_to=" + reply_to +
                ", isExpend=" + isExpend +
                '}';
    }
}
