package com.example.blog.model;

public class BlogModel {
    String id;
    String title;
    String desc;
    boolean fav;
    boolean bookmarked;

    public BlogModel(String id, String title, String desc, boolean fav, boolean bookmarked) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.fav = fav;
        this.bookmarked = bookmarked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
}
